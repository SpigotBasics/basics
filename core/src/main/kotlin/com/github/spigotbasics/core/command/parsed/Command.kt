package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.CommandResult

class Command<T : CommandContext>(
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

    fun execute(input: List<String>): Either<CommandResult, ParseResult.Failure> {
        // Sort paths by the number of arguments they expect, ascending.
        val sortedPaths = paths.sortedBy { it.arguments.size }

        var shortestPathFailure: ParseResult.Failure? = null

        sortedPaths.forEach { path ->
            if (path.matches(input)) {
                when (val result = path.parse(input)) {
                    is ParseResult.Success -> {
                        executor.execute(result.context)
                        return Either.Left(CommandResult.SUCCESS)
                    }
                    is ParseResult.Failure -> {
                        // Optionally handle or display errors if necessary for debugging
                        result.errors.forEach { println(it) }
                        if(shortestPathFailure == null /*|| result.errors.size < shortestPathFailure!!.errors.size*/) {
                            shortestPathFailure = result
                        }
                    }
                }
            }
        }
        // If no paths matched, inform the user or handle the failure
        println("No matching command format found.")
        if(shortestPathFailure != null) {
            return Either.Right(shortestPathFailure!!)
        }
        return Either.Left(CommandResult.USAGE)
    }

    fun tabComplete(input: List<String>): List<String> {
        // Attempt to find the best matching ArgumentPath for the current input
        // and return its tab completions.
        val completions = mutableListOf<String>()
        for (path in paths) {
            completions.addAll(path.tabComplete(input))
        }
        // Remove duplicates and return
        return completions.distinct()
    }
}
