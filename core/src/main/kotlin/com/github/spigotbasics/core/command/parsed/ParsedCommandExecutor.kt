package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

class ParsedCommandExecutor<T : CommandContext>(
    private val executor: CommandExecutor<T>,
    private val paths: List<ArgumentPath<T>>,
) {
//    fun execute(input: List<String>) {
//        for (path in paths) {
//            val context = path.parse(input)
//            if (context != null) {
//                executor.execute(context)
//                return
//            }
//        }
//        // Handle no matching path found, e.g., show usage or error message
//    }

//    fun execute(input: List<String>): Either<CommandResult, ParseResult.Failure> {
//        for (path in paths) {
//            when (val result = path.parse(input)) {
//                is ParseResult.Success -> {
//                    executor.execute(result.context)
//                    return Either.Left(CommandResult.SUCCESS)
//                }
//                is ParseResult.Failure -> {
//                    // Handle or display errors
//                    result.errors.forEach { println(it) }
//                    return Either.Right(result)
//                }
//            }
//        }
//        // If no paths matched, optionally print a generic error or usage message
//        println("Invalid command syntax.")
//        return Either.Left(CommandResult.USAGE)
//    }

    fun execute(
        sender: CommandSender,
        input: List<String>,
    ): Either<CommandResult, ParseResult.Failure> {
        // Empty args = show usage, unless an empty path is registered
        if (input.isEmpty()) {
            if (!paths.any { it.arguments.isEmpty() }) {
                return Either.Left(CommandResult.USAGE)
            }
        }

        // Sort paths by the number of arguments they expect, ascending.
        val sortedPaths = paths.sortedBy { it.arguments.size }

        var shortestPathFailure: ParseResult.Failure? = null
        var bestMatchResult: PathMatchResult? = null
        var errors: List<Message>? = null
        var missingPermission: Permission? = null

        sortedPaths.forEach { path ->
            val matchResult = path.matches(sender, input)
            if (matchResult is Either.Right) {
                // TODO: Maybe collect all error messages? Right now, which error message is shown depends on the order of the paths
                //  That means more specific ones should be registered first
                val newErrors = matchResult.value
                if (errors == null || errors!!.size > newErrors.size) {
                    errors = newErrors
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
                when (val result = path.parse(sender, input)) {
                    is ParseResult.Success -> {
                        executor.execute(sender, result.context)
                        // println("Command executed successfully.")
                        return Either.Left(CommandResult.SUCCESS)
                    }

                    is ParseResult.Failure -> {
                        // println("Path failed to parse: $result")
                        // result.errors.forEach { println(it) } // Optionally handle or display errors if necessary for debugging

                        // Might comment out the part after || below v v v
                        if (shortestPathFailure == null || result.errors.size < shortestPathFailure!!.errors.size) {
                            // println("Shortest path failure: $result")
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
            // println("No matching command format found.")
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
            completions.addAll(path.tabComplete(input))
        }
        // Remove duplicates and return
        return completions.distinct()
    }
}
