package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.lastOrEmpty
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

class ArgumentPath<T : CommandContext>(
    val senderArgument: SenderType<*>,
    val arguments: List<CommandArgument<*>>,
    // TODO: Check permission for specific paths!
    val permission: Permission? = null,
    private val contextBuilder: (CommandSender, List<Any?>) -> T,
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

    fun matches(
        sender: CommandSender,
        args: List<String>,
    ): Either<PathMatchResult, List<Message>> {
        // Exact match for the number of arguments
        if (args.size > arguments.size) return Either.Left(PathMatchResult.NO) // Maybe use != ? > allows to show "missing item", != wouldn't
        // TODO: Keep a list of non-matches where size is too little, and if no other errors occur, say "missing item", only otherwise
        //  fallback to CommandResult.USAGE

        // Each provided arg must be parseable by its corresponding CommandArgument
        val errors = mutableListOf<Message>()
        // val matches =  // used to be all(...)
        args.indices.forEach { index ->
            val parsed = arguments[index].parse(args[index])

            if (parsed == null) {
                val error = arguments[index].errorMessage(args[index])
                errors.add(error)
            }

            // true
        }

        if (errors.isNotEmpty()) return Either.Right(errors)

        if (!senderArgument.requiredType.isInstance(sender)) return Either.Left(PathMatchResult.YES_BUT_NOT_FROM_CONSOLE)
        return Either.Left(PathMatchResult.YES)
    }

//    fun parse(args: List<String>): T? {
//        if (!matches(args)) return null
//        val parsedArgs =
//            arguments.zip(args).mapNotNull { (arg, value) ->
//                arg.parse(value)
//            }
//        return contextBuilder(parsedArgs)
//    }

    fun parse(
        sender: CommandSender,
        args: List<String>,
    ): ParseResult<T> {
        if (!senderArgument.requiredType.isInstance(sender)) {
            return ParseResult.Failure(listOf(Basics.messages.commandNotFromConsole))
        }

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
            return ParseResult.Success(contextBuilder(sender, parsedArgs))
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
