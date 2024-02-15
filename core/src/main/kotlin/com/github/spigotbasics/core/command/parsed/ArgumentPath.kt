package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.command.parsed.context.CommandContext
import com.github.spigotbasics.core.extensions.lastOrEmpty
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission
import kotlin.math.min

class ArgumentPath<T : CommandContext>(
    val senderArgument: SenderType<*>,
    val arguments: List<Pair<String, CommandArgument<*>>>,
    val permission: List<Permission> = emptyList(),
    private val contextBuilder: (Map<String, Any?>) -> T,
) {
    companion object {
        val logger = BasicsLoggerFactory.getCoreLogger(ArgumentPath::class)
    }

    init {
        if (arguments.any { it.first == "sender" }) {
            throw IllegalArgumentException("Argument name 'sender' is reserved")
        }
    }

    fun matches(
        sender: CommandSender,
        args: List<String>,
    ): Either<PathMatchResult, Pair<Int, List<Message>>> {
        val greedyArg = arguments.indexOfFirst { it.second.greedy }
        var firstErrorIndex = -1

        // Exact match for the number of arguments, unless one argument is greedy
//        if (greedyArg == -1 && args.size > arguments.size) {
//            return Either.Left(
//                PathMatchResult.NO,
//            )
//        }

        // Each provided arg must be parseable by its corresponding CommandArgument
        val errors = mutableListOf<Message>()
        arguments.indices.forEach { index ->

            if (index >= args.size) {
                return@forEach // Needed because we now iterate over arguments and not args anymore
            }

            // val parsed = arguments[index].parse(args[index])
            val (_, argument) = arguments[index]

            val currentArg = accumulateArguments(index, args, arguments.map { it.second }, greedyArg)

            val parsed = argument.parse(sender, currentArg)

            if (parsed == null) {
                val error = argument.errorMessage(sender, currentArg)
                firstErrorIndex = if (firstErrorIndex == -1) index else min(firstErrorIndex, index)
                errors.add(error)
            }
        }

        if (errors.isNotEmpty()) return Either.Right(firstErrorIndex to errors)

        if (greedyArg == -1) {
            if (args.size > arguments.size) {
                return Either.Right(
                    arguments.size to listOf(Basics.messages.tooManyArguments),
                )
            }
        }

        if (!senderArgument.requiredType.isInstance(sender)) return Either.Left(PathMatchResult.YES_BUT_NOT_FROM_CONSOLE)
        if (!hasPermission(sender)) return Either.Left(PathMatchResult.YES_BUT_NO_PERMISSION)
        return Either.Left(PathMatchResult.YES)
    }

    fun accumulateArguments(
        argIndex: Int,
        givenArgs: List<String>,
        commandArguments: List<CommandArgument<*>>,
        greedyPosition: Int,
    ): String {
        val result = accumulateArguments0(argIndex, givenArgs, commandArguments, greedyPosition)
        logger.debug(400, "Accumulated arguments @ $argIndex  ----- $result")
        return result
    }

    fun accumulateArguments0(
        argIndex: Int,
        givenArgs: List<String>,
        commandArguments: List<CommandArgument<*>>,
        greedyPosition: Int,
    ): String {
        if (greedyPosition == -1) {
            return givenArgs[argIndex]
        }
        if (argIndex < greedyPosition) {
            return givenArgs[argIndex]
        }
        val greedyArgumentExtraSize = givenArgs.size - commandArguments.size
        val extraArgs = givenArgs.subList(greedyPosition, greedyPosition + greedyArgumentExtraSize + 1)

        logger.debug(
            600,
            "Accumulating arguments: argIndex: $argIndex, givenArgs: $givenArgs, commandArguments: $commandArguments, " +
                "greedyPosition: $greedyPosition",
        )
        logger.debug(500, "GreedyArgumentExtraSize: $greedyArgumentExtraSize, extraArgs: $extraArgs")

        if (argIndex == greedyPosition) {
            return extraArgs.joinToString(" ")
        }

        return givenArgs[argIndex + greedyArgumentExtraSize]
    }

    fun parse(
        sender: CommandSender,
        args: List<String>,
    ): ParseResult<T> {
        logger.debug(
            10,
            "ArgumentPath#parse: sender: $sender, args: $args, senderArgument: $senderArgument, arguments: $arguments, " +
                "permission: $permission, contextBuilder: $contextBuilder",
        )

        val greedyArg = arguments.indexOfFirst { it.second.greedy } // TODO: Can be field

        if (!senderArgument.requiredType.isInstance(sender)) {
            logger.debug(10, "Failure: senderArgument.requiredType.isInstance(sender) is false")
            return ParseResult.Failure(listOf(Basics.messages.commandNotFromConsole))
        }

        val parsedArgs = mutableMapOf<String, Any?>()
        parsedArgs["sender"] = sender
        val errors = mutableListOf<Message>()

        for ((index, argumentPair) in arguments.withIndex()) {
            val (argName, arg) = argumentPair
            if (index >= args.size) {
                logger.debug(10, "Failure: index >= args.size")
                errors.add(Basics.messages.missingArgument(arg.name))
                break
            }

            val currentArg = accumulateArguments(index, args, arguments.map { it.second }, greedyArg)

            val parsed = arg.parse(sender, currentArg)
            if (parsed == null) {
                logger.debug(10, "Failure: parsed == null for arg: $arg, args[$index]: ${args[index]} (index: $index)")
                errors.add(arg.errorMessage(sender, currentArg))
                break
            } else {
                logger.debug(10, "  parsed: $parsed for arg: $arg, args[$index]: ${args[index]} (index: $index)")
                parsedArgs[argName] = parsed
            }
        }

        return if (errors.isEmpty() &&
            parsedArgs.size == arguments.size + 1 // One extra for the sender
        ) {
            logger.debug(10, "Success: errors.isEmpty() && parsedArgs.size == arguments.size + 1")
            ParseResult.Success(contextBuilder(parsedArgs))
        } else {
            logger.debug(10, "Failure: errors.isNotEmpty() || parsedArgs.size != arguments.size")
            logger.debug(10, "Errors empty: ${errors.isEmpty()}")
            logger.debug(10, "Parsed args size: ${parsedArgs.size}, arguments size: ${arguments.size}")
            if (errors.isEmpty()) {
                // errors.add(coreMessages.commandArgumentSizeMismatch)
                throw IllegalStateException("parsedArgs.size != arguments.size +1, but errors.isEmpty()")
            }
            ParseResult.Failure(errors)
        }
    }

    /**
     * Checks if this path matches the input until the end of the input. This is only used for tabcompletes.
     *
     * @param sender
     * @param input
     * @return
     */
    fun matchesStart(
        sender: CommandSender,
        input: List<String>,
    ): Boolean {
        logger.debug(200, "TabComplete matchesStart: input: $input @ $this")
        if (input.size > arguments.size) {
            logger.debug(200, "  input.size > arguments.size")
            return false
        }
        input.forEachIndexed { index, s ->
            logger.debug(200, "  Checking index $index, s: $s")
            if (index == input.size - 1) {
                logger.debug(200, "    Last argument, skipping")
                return@forEachIndexed
            } // Last argument is still typing
            val arg = arguments[index].second
            val parsed = arg.parse(sender, s)
            if (parsed == null) {
                logger.debug(200, "     parsed == null")
                return false
            }
        }
        logger.debug(200, "  All arguments parsed, this path matches the input")
        return true
    }

    fun tabComplete(
        sender: CommandSender,
        args: List<String>,
    ): List<String> {
        if (args.isEmpty() || args.size > arguments.size) return emptyList()

        val currentArgIndex = args.size - 1
        return arguments[currentArgIndex].second.tabComplete(sender, args.lastOrEmpty())
    }

    fun isCorrectSender(sender: CommandSender): Boolean {
        return senderArgument.requiredType.isInstance(sender)
    }

    fun hasPermission(sender: CommandSender): Boolean {
        return permission.all { sender.hasPermission(it) }
    }

    override fun toString(): String {
        return "ArgumentPath(senderArgument=$senderArgument, arguments=$arguments, permission=$permission)"
    }


}
