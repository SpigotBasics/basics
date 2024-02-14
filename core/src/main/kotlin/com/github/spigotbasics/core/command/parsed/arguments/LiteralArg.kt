package com.github.spigotbasics.core.command.parsed.arguments

class LiteralArg(override val name: String) : CommandArgument<String>(name) {
    override fun parse(value: String): String? {
        return if (value.equals(name, ignoreCase = true)) {
            value
        } else {
            null
        }
    }

    private val tabList = listOf(name)

    override fun tabComplete(typing: String): List<String> {
        return if (name.startsWith(typing, ignoreCase = true)) {
            tabList
        } else {
            emptyList()
        }
    }
}
