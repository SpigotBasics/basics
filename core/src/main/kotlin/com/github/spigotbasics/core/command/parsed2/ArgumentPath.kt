package com.github.spigotbasics.core.command.parsed2

class ArgumentPath<T : CommandContext>(
    private val arguments: List<CommandArgument<*>>,
    private val contextBuilder: (List<Any?>) -> T
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

    fun parse(args: List<String>): T? {
        if (!matches(args)) return null
        val parsedArgs = arguments.zip(args).mapNotNull { (arg, value) ->
            arg.parse(value)
        }
        return contextBuilder(parsedArgs)
    }
}