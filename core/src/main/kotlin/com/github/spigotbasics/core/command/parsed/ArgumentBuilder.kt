package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.command.parsed.arguments.LiteralArg

class ArgumentBuilder {
    companion object {
        private const val UNUSED_ARG_PREFIX = "_unused_"
    }

    private var unusedArgIndex = 0
    private val arguments = mutableListOf<Pair<String, CommandArgument<*>>>()

    private fun generateUnusedArgName(): String = "${UNUSED_ARG_PREFIX}${unusedArgIndex++}"

    fun add(
        name: String,
        commandArgument: CommandArgument<*>,
    ) {
        arguments.add((name) to commandArgument)
    }

    fun add(commandArgument: CommandArgument<*>) {
        arguments.add((generateUnusedArgName()) to commandArgument)
    }

    fun literal(value: String) = LiteralArg(value)

    fun build(): List<Pair<String, CommandArgument<*>>> = arguments
}
