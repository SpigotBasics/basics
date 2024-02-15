package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.command.parsed.context.CommandContext
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

class ParsedCommandExecutor<T : CommandContext>(
    private val executor: CommandContextExecutor<T>?,
    private val paths: List<ArgumentPath<T>>,
) {
    companion object {
        private val logger = BasicsLoggerFactory.getCoreLogger(ParsedCommandExecutor::class)
    }

    init {
        logger.debug(10, "ParsedCommandExecutor created with paths: ${paths.size}")
    }

    fun execute(
        sender: CommandSender,
        input: List<String>,
    ): Either<CommandResult, ParseResult.Failure> {
        logger.debug(10, "ParsedCommandExecutor executing with input: $input")

        // Empty args = show usage, unless an empty path is registered
        if (input.isEmpty()) {
            if (!paths.any { it.arguments.isEmpty() }) {
                logger.debug(10, "Input is empty and no empty path was found")
                return Either.Left(CommandResult.USAGE)
            }
        }

        // Sort paths by the number of arguments they expect
        val sortedPaths = paths.sortedBy { -it.arguments.size }

        var shortestPathFailure: ParseResult.Failure? = null
        var bestMatchResult: PathMatchResult? = null
        var errors: List<Message>? = null
        var missingPermission: Permission? = null
        var lastErrorIndex = -1

        sortedPaths.forEach { path ->

            logger.debug(10, " Testing path match $path ??")

            val matchResult = path.matches(sender, input)
            logger.debug(10, "Match result: $matchResult")

            if (matchResult is Either.Right) {
                // TODO: Maybe collect all error messages? Right now, which error message is shown depends on the order of the paths
                //  That means more specific ones should be registered first
                val (firstErrorIndex, newErrors) = matchResult.value
                if (lastErrorIndex == -1 ||
                    // errors!!.size > newErrors.size // Old version - use the one with the least errors
                    lastErrorIndex < firstErrorIndex
                ) {
                    errors = newErrors
                    lastErrorIndex = firstErrorIndex
                }
            } else if (matchResult is Either.Left &&
                (
                    matchResult.value == PathMatchResult.YES_BUT_NOT_FROM_CONSOLE ||
                        matchResult.value == PathMatchResult.YES_BUT_NO_PERMISSION
                )
            ) {
                bestMatchResult = matchResult.value
                if (matchResult.value == PathMatchResult.YES_BUT_NO_PERMISSION) {
                    missingPermission = path.permission.firstOrNull { !sender.hasPermission(it) }
                }
            } else if (matchResult is Either.Left && matchResult.value == PathMatchResult.YES) {
                logger.debug(10, "Path matched: $path")
                logger.debug(10, "Now executing it...")

                when (val result = path.parse(sender, input)) {
                    is ParseResult.Success -> {
                        logger.debug(10, "Path.parse == ParseResult.Success: $result")

                        val actualExecutor = path.ownExecutor ?: executor ?: error("No executor found for path: $path")

                        actualExecutor.execute(sender, result.context)
                        // logger.debug(10,"Command executed successfully.")
                        logger.debug(10, "Command executed successfully.")
                        return Either.Left(CommandResult.SUCCESS)
                    }

                    is ParseResult.Failure -> {
                        logger.debug(10, "Path.parse == ParseResult.Failure: $result")

                        // logger.debug(10,"Path failed to parse: $result")
                        // result.errors.forEach { logger.debug(10,it) } // Optionally handle or display errors if necessary for debugging

                        // Might comment out the part after || below v v v
                        if (shortestPathFailure == null || result.errors.size < shortestPathFailure!!.errors.size) {
                            // logger.debug(10,"Shortest path failure: $result")
                            shortestPathFailure = result
                        }
                    }
                }
            }
        }
        // If no paths matched, inform the user or handle the failure

        if (shortestPathFailure != null) {
            return Either.Right(shortestPathFailure!!)
        }

        // TODO: Maybe this must be moved up
        if (bestMatchResult == PathMatchResult.YES_BUT_NOT_FROM_CONSOLE) {
            return Either.Left(CommandResult.NOT_FROM_CONSOLE)
        }
        if (bestMatchResult == PathMatchResult.YES_BUT_NO_PERMISSION && missingPermission != null) {
            return Either.Left(CommandResult.noPermission(missingPermission!!))
        }

        if (errors != null) {
            errors!![0].sendToSender(sender)
            return Either.Left(CommandResult.SUCCESS)
        } else {
            // logger.debug(10,"No matching command format found.")
            return Either.Left(CommandResult.USAGE)
        }
    }

    fun tabComplete(
        sender: CommandSender,
        input: List<String>,
    ): List<String> {
        // Attempt to find the best matching ArgumentPath for the current input
        // and return its tab completions.
        val completions = mutableListOf<String>()
        for (path in paths) {
            if (!path.isCorrectSender(sender)) continue
            if (!path.hasPermission(sender)) continue
            if (!path.matchesStart(sender, input)) continue
            completions.addAll(path.tabComplete(sender, input))
        }
        // Remove duplicates and return
        return completions.distinct()
    }
}
