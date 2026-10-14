package com.github.spigotbasics.core.command.parsed.arguments

import org.bukkit.command.CommandSender

open class FloatArg(name: String) : CommandArgument<Float>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Float? = value.toFloatOrNull()?.takeIf { it.isFinite() }
}
