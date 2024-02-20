package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.command.parsed.context.CommandContext
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
    val ownExecutor: CommandContextExecutor<T>? = null,
) {
    companion object {
        val logger = BasicsLoggerFactory.getCoreLogger(ArgumentPath::class)
    }

    init {
        require(arguments.none { it.first == "sender" }) { "Argument name 'sender' is reserved" }
    }

    fun matches(
        sender: CommandSender,
        args: List<String>,
    ): Either<PathMatchResult, Pair<Double, List<Message>>> {
        val greedyArg = arguments.indexOfFirst { it.second.greedy }
        var firstErrorIndex = -1.0

        // Each provided arg must be parseable by its corresponding CommandArgument
        val errors = mutableListOf<Message>()
        arguments.indices.forEach { index ->

            if (index >= args.size) {
                return@forEach // Needed because we now iterate over arguments and not args anymore
            }

            // val parsed = arguments[index].parse(args[index])
            val (_, argument) = arguments[index]

            val currentArgResult = accumulateArguments(index, args, arguments.map { it.second }, greedyArg)

            if (currentArgResult is Either.Right) {
                errors.add(Basics.messages.notEnoughArgumentsGivenForArgument(argument.name))
                val missingArguments = currentArgResult.value
                // "First error" is the first missing argument, adjusted by 0.5 so that arguments with matching paths are preferred
                val returnValue = (index + 1) - missingArguments + 0.5
                firstErrorIndex = if (firstErrorIndex == -1.0) returnValue else min(firstErrorIndex, returnValue) // TODO - is this correct?
                return@forEach
            }

            val currentArg = currentArgResult.leftOrNull()!!

            val parsed = argument.parse(sender, currentArg)

            if (parsed == null) {
                val error = argument.errorMessage(sender, currentArg)
                firstErrorIndex = if (firstErrorIndex == -1.0) index.toDouble() else min(firstErrorIndex, index.toDouble())
                errors.add(error)
            }
        }

        if (errors.isNotEmpty()) return Either.Right(firstErrorIndex to errors)

        if (greedyArg == -1) {
            if (args.size > arguments.sumOf { it.second.length }) {
                return Either.Right(
                    arguments.size.toDouble() to listOf(Basics.messages.tooManyArguments),
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
    ): Either<String, Int> {
        val result = accumulateArguments0(argIndex, givenArgs, commandArguments, greedyPosition)
        logger.debug(400, "Accumulated arguments @ $argIndex  ----- $result")
        return result
    }

    fun accumulateArguments0(
        argIndex: Int,
        givenArgs: List<String>,
        commandArguments: List<CommandArgument<*>>,
        greedyPosition: Int,
    ): Either<String, Int> {
        // Count length of combined arguments before this one
        var myStartIndex = commandArguments.subList(0, argIndex).sumOf { it.length }
        val mySupposedLength = commandArguments[argIndex].length
        val myEndIndex = myStartIndex + mySupposedLength
        var missingArguments = mySupposedLength - (givenArgs.size - myStartIndex)

        if (myEndIndex > givenArgs.size) {
            return Either.Right(missingArguments) // TODO: Is this correct?
        }

        val expectedLengthWithoutGreedy = commandArguments.filter { !it.greedy }.sumOf { it.length }

        if (greedyPosition == -1 || argIndex < greedyPosition) {
            return Either.Left(givenArgs.subList(myStartIndex, myEndIndex).joinToString(" "))
        }

        val greedyArgumentSize = givenArgs.size - expectedLengthWithoutGreedy
        val extraArgs = givenArgs.subList(greedyPosition, greedyPosition + greedyArgumentSize)

        logger.debug(
            600,
            "Accumulating arguments: argIndex: $argIndex, givenArgs: $givenArgs, commandArguments: $commandArguments, " +
                "greedyPosition: $greedyPosition",
        )
        logger.debug(500, "GreedyArgumentSize: $greedyArgumentSize, extraArgs: $extraArgs")

        if (argIndex == greedyPosition) {
            return Either.Left(extraArgs.joinToString(" "))
        }

        val lengthAfterMe = commandArguments.subList(argIndex + 1, commandArguments.size).sumOf { it.length }
        myStartIndex = givenArgs.size - lengthAfterMe - 1

        missingArguments = mySupposedLength - (givenArgs.size - myStartIndex)

        if (myStartIndex + 1 > givenArgs.size) {
            return Either.Right(missingArguments)
        }

        return Either.Left(givenArgs.subList(myStartIndex, givenArgs.size).joinToString(" "))
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

        val parsedArgs = mutableMapOf<String, Any?>("sender" to sender)
        val errors = mutableListOf<Message>()

        for ((index, argumentPair) in arguments.withIndex()) {
            val (argName, arg) = argumentPair
            if (index >= args.size) {
                logger.debug(10, "Failure: index >= args.size")
                errors.add(Basics.messages.missingArgument(arg.name))
                break
            }

            val currentArgResult = accumulateArguments(index, args, arguments.map { it.second }, greedyArg)

            if (currentArgResult is Either.Right) {
                errors.add(Basics.messages.notEnoughArgumentsGivenForArgument(arg.name))
                break
            }

            val currentArg = currentArgResult.leftOrNull()!!

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
        if (!isCorrectSender(sender) || !hasPermission(sender)) return false

        logger.debug(300, ">> Matching start: $this")
        var accumulatedInputCount = 0
        for ((index, pair) in arguments.withIndex()) {
            val (_, argument) = pair
            val isLastArgument = index == input.size - 1 || index == arguments.size - 1

            // Calculate the expected count of inputs for this argument
            val expectedCount = if (argument.greedy) input.size - accumulatedInputCount else argument.length
            val availableInputs = input.drop(accumulatedInputCount).take(expectedCount)
            val argumentInput = availableInputs.joinToString(" ")

            // Update the accumulated input count for the next argument
            accumulatedInputCount += availableInputs.size

            logger.debug(400, "accumulatedInputCount: $accumulatedInputCount")
            logger.debug(400, "expectedCount: $expectedCount")
            logger.debug(400, "availableInputs: $availableInputs")
            logger.debug(400, "argumentInput: $argumentInput")
            logger.debug(400, "isLastArgument: $isLastArgument")

            // If it's not the last argument and parsing fails, return false
            if (!isLastArgument && argument.parse(sender, argumentInput) == null) {
                logger.debug(400, "Failure: !isLastArgument && argument.parse(sender, argumentInput) == null")
                return false
            }

            // If it's the last argument, it's okay if parse returns null (user might still be typing)
            if (isLastArgument) {
                // If there's enough input for the argument to potentially parse, return true (assume user might complete it)
                // If argument is greedy, always return true since we're accommodating partial input
                if (argument.greedy || argument.parse(sender, argumentInput) != null) {
                    logger.debug(400, "Success: isLastArgument && (argument.greedy || argument.parse(sender, argumentInput) != null)")
                    return true
                } else {
                    // For non-greedy last argument, it's okay if parsing fails due to partial input
                    logger.debug(400, "Failure: isLastArgument && ! (argument.greedy || argument.parse(sender, argumentInput) != null)")
                    return availableInputs.size >= argument.length
                }
            }
        }

        // If the loop completes without returning, all arguments except possibly the last have parsed successfully
        logger.debug(400, "-> true")
        return true
    }

//    fun matchesStart(
//        sender: CommandSender,
//        input: List<String>,
//    ): Boolean {
//        if (!isCorrectSender(sender) || !hasPermission(sender)) return false
//
//        var currentIndex = 0
//        arguments.forEachIndexed { index, (_, argument) ->
//            if (!argument.greedy) {
//                // Check if current argument can be fully covered by remaining input
//                if (currentIndex + argument.length > input.size) {
//                    // If this is the last argument or a greedy argument is next and the user is still typing it, partial input is okay
//                    if (index == arguments.size - 1 || (arguments.getOrNull(index + 1)?.second?.greedy ?: false)) {
//                        return true // Accept partial input for the last non-greedy argument or before a greedy one
//                    }
//                    return false // Not enough input for this non-greedy argument
//                }
//                currentIndex += argument.length
//            } else {
//                // For a greedy argument, ensure at least its minimum length is available
//                if (currentIndex + argument.length > input.size) {
//                    // Allow partial input for greedy arguments if it's the last argument
//                    return index == arguments.size - 1
//                }
//                // Greedy argument consumes the rest; no need to check further
//                return true
//            }
//        }
//
//        // If all non-greedy arguments up to the last one were satisfied with correct length, or a greedy argument had enough input
//        return true
//    }

    fun tabComplete(
        sender: CommandSender,
        input: List<String>,
    ): List<String> {
        if (!isCorrectSender(sender) || !hasPermission(sender)) return emptyList()

        // Identify the argument the user is currently completing
        val currentIndex = input.size - 1
        var accumulatedIndex = 0

        logger.debug(300, "Tabcompleting:")
        logger.debug(300, "ArgumentPath: $this")
        logger.debug(300, "currentIndex: $currentIndex")
        logger.debug(300, "input: $input")

        arguments.forEachIndexed { index, (argName, argument) ->
            val isLastArgument = index == arguments.size - 1
            val isGreedyArgument = argument.greedy

            // Calculate the start and end index for the current argument's input
            val startIndex = accumulatedIndex
            var endIndex = accumulatedIndex + argument.length

            // If the argument is greedy, or we are at the last argument, adjust endIndex to include all remaining input
            if (isGreedyArgument || isLastArgument) endIndex = input.size

            // Update accumulatedIndex for the next iteration
            accumulatedIndex += argument.length

            logger.debug(300, "argName: $argName")
            logger.debug(300, "startIndex: $startIndex")
            logger.debug(300, "endIndex: $endIndex")
            logger.debug(300, "accumulatedIndex: $accumulatedIndex")

            // Check if the current argument is the one being completed
            if (currentIndex >= startIndex && currentIndex < endIndex) {
                // For greedy and last arguments, include all remaining input; otherwise, limit to the argument's length
                val relevantInput = if (currentIndex < input.size) input.subList(startIndex, endIndex).joinToString(" ") else ""
                return argument.tabComplete(sender, relevantInput)
            }
        }

        return emptyList()
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
