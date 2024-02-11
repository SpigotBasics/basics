package com.github.spigotbasics.core.command.parsed

class ArgumentPath<T : CommandContext>(
    private val arguments: List<CommandArgument<*>>,
    private val contextBuilder: (List<Any?>) -> T,
) {
    fun matches(args: List<String>): Boolean {
        if (args.size != arguments.size) return false
        return arguments.zip(args).all { (arg, value) ->
            try {
                arg.parse(value) != null
            } catch (e: Exception) {
                false
            }
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
        val errors = mutableListOf<String>()

        for ((index, arg) in arguments.withIndex()) {
            if (index >= args.size) {
                errors.add("Missing argument for ${arg::class.simpleName}")
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
        // If the current args size is less than or equal to the arguments size,
        // suggest completions for the current argument.
        if (args.size <= arguments.size) {
            val currentArgIndex = args.size - 1
            // If exactly equal, user is typing the next argument (or just started typing the first one)
            return if (args.size == arguments.size) {
                arguments.getOrNull(currentArgIndex)?.tabComplete() ?: emptyList()
            } else {
                // User is in the middle of typing an argument; suggest completions for this arg
                arguments.getOrNull(currentArgIndex)?.tabComplete()?.filter {
                    it.startsWith(args.last(), ignoreCase = true)
                } ?: emptyList()
            }
        }
        return emptyList()
    }
}
