package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import org.bukkit.command.CommandSender

open class LiteralArg(name: String) : CommandArgument<String>(name) {

    companion object {
        private val logger = BasicsLoggerFactory.getCoreLogger(LiteralArg::class)
    }

    override fun parse(
        sender: CommandSender,
        value: String,
    ): String? {
        return if (value.equals(name, ignoreCase = true)) {
            value
        } else {
            null
        }
    }

    private val tabList = listOf(name)

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        logger.debug(400, "$this @ tabComplete: sender=$sender, typing=$typing")
        return if (name.startsWith(typing, ignoreCase = true)) {
            tabList
        } else {
            emptyList()
        }
    }

    override fun toString(): String {
        return "LiteralArg(name='$name')"
    }
}
