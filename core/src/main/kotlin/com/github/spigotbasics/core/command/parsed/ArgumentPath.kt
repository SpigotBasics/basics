package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.lastOrEmpty
import com.github.spigotbasics.core.messages.Message

class ArgumentPath<T : CommandContext>(
    val arguments: List<CommandArgument<*>>,
    private val contextBuilder: (List<Any?>) -> T,
) {
//    fun matches(args: List<String>): Boolean {
//        if (args.size > arguments.size) return false
//
//        for ((index, arg) in args.withIndex()) {
//            if (arguments[index].parse(arg) == null) return false
//        }
//
//        return true
//    }

    fun matches(args: List<String>): Boolean {
        // Exact match for the number of arguments
        if (args.size > arguments.size) return false // Maybe use != ?

        // Each provided arg must be parseable by its corresponding CommandArgument
        return args.indices.all { index ->
            arguments[index].parse(args[index]) != null
        }
    }

//    fun parse(args: List<String>): T? {
//        if (!matches(args)) return null
//        val parsedArgs =
//            arguments.zip(args).mapNotNull { (arg, value) ->
//                arg.parse(value)
//            }
//        return contextBuilder(parsedArgs)
//    }

    fun parse(args: List<String>): ParseResult<T> {
        val parsedArgs = mutableListOf<Any?>()
        val errors = mutableListOf<Message>()

        for ((index, arg) in arguments.withIndex()) {
            if (index >= args.size) {
//                errors.add("Missing argument for ${arg.name}")
                errors.add(Basics.messages.missingArgument(arg.name))
                break
            }

            val parsed = arg.parse(args[index])
            if (parsed == null) {
                errors.add(arg.errorMessage(args[index]))
                break
            } else {
                parsedArgs.add(parsed)
            }
        }

        if (errors.isEmpty() && parsedArgs.size == arguments.size) {
            return ParseResult.Success(contextBuilder(parsedArgs))
        } else {
            return ParseResult.Failure(errors)
        }
    }

    fun tabComplete(args: List<String>): List<String> {
        if (args.isEmpty() || args.size > arguments.size) return emptyList()

        val currentArgIndex = args.size - 1
        return arguments[currentArgIndex].tabComplete(args.lastOrEmpty())
    }
}
