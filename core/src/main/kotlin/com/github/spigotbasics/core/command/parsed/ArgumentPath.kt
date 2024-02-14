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
    ): Either<PathMatchResult, List<Message>> {
        // Exact match for the number of arguments
        if (args.size > arguments.size) {
            return Either.Left(
                PathMatchResult.NO,
            )
        }

        // Each provided arg must be parseable by its corresponding CommandArgument
        val errors = mutableListOf<Message>()
        args.indices.forEach { index ->
            // val parsed = arguments[index].parse(args[index])
            val (_, argument) = arguments[index]
            val parsed = argument.parse(args[index])

            if (parsed == null) {
                val error = argument.errorMessage(args[index])
                errors.add(error)
            }
        }

        if (errors.isNotEmpty()) return Either.Right(errors)

        if (!senderArgument.requiredType.isInstance(sender)) return Either.Left(PathMatchResult.YES_BUT_NOT_FROM_CONSOLE)
        if (!hasPermission(sender)) return Either.Left(PathMatchResult.YES_BUT_NO_PERMISSION)
        return Either.Left(PathMatchResult.YES)
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
                errors.add(Basics.messages.missingArgument(arg.getArgumentName()))
                break
            }

            val parsed = arg.parse(args[index])
            if (parsed == null) {
                logger.debug(10, "Failure: parsed == null for arg: $arg, args[$index]: ${args[index]} (index: $index)")
                errors.add(arg.errorMessage(args[index]))
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

    fun tabComplete(args: List<String>): List<String> {
        if (args.isEmpty() || args.size > arguments.size) return emptyList()

        val currentArgIndex = args.size - 1
        return arguments[currentArgIndex].second.tabComplete(args.lastOrEmpty())
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
