package com.github.spigotbasics.core.command.parsed.dsl

import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.command.parsed.arguments.LiteralArg

class ArgumentBuilder {
    companion object {
        private const val UNUSED_ARG_PREFIX = "_unused_"
    }

    private var unusedArgIndex = 0
    private val arguments = mutableListOf<Pair<String, CommandArgument<*>>>()

    private fun generateUnusedArgName(): String = "$UNUSED_ARG_PREFIX${unusedArgIndex++}"

    fun named(
        name: String,
        commandArgument: CommandArgument<*>,
    ) {
        arguments.add((name) to commandArgument)
    }

    fun named(
        commandArgument: CommandArgument<*>,
        name: String,
    ) = named(name, commandArgument)

    fun unnamed(commandArgument: CommandArgument<*>) {
        arguments.add((generateUnusedArgName()) to commandArgument)
    }

    fun literal(value: String) = LiteralArg(value)

    // TODO: Nested commands could work sth like this
//    fun sub(
//        name: String,
//        block: ArgumentBuilder.() -> Unit,
//    ) {
//        val argumentBuilder = ArgumentBuilder()
//        argumentBuilder.block()
//        arguments.add((name) to argumentBuilder.build())
//    }

    /**
     * Creates a [LiteralArg] named "sub". If [level] is not 0, the name will be "sub$level".
     *
     * @param name
     * @param level
     */
    fun sub(
        name: String,
        level: Int = 0,
    ) {
        val nameWithLevel = if (level == 0) "sub" else "sub$level"
        arguments.add((nameWithLevel) to LiteralArg(name))
    }

    fun build(): List<Pair<String, CommandArgument<*>>> = arguments
}
