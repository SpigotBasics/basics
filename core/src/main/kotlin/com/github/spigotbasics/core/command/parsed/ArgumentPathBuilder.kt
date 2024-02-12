package com.github.spigotbasics.core.command.parsed

import org.bukkit.command.CommandSender
import org.bukkit.permissions.Permission

class ArgumentPathBuilder<T : ParsedCommandContext> {
    private var senderType: SenderType<*> = AnySender
    private var arguments = emptyList<CommandArgument<*>>()
    private var permissions = emptyList<Permission>()
    private var contextBuilder: ((CommandSender, List<Any?>) -> T)? = null

    private fun senderType(senderType: SenderType<*>) = apply { this.senderType = senderType }

    fun playerOnly() = apply { senderType(PlayerSender) }

    fun permissions(vararg permissions: Permission) = apply { this.permissions = permissions.toList() }

    fun arguments(vararg arguments: CommandArgument<*>) = apply { this.arguments = arguments.toList() }

    fun contextBuilder(contextBuilder: (CommandSender, List<Any?>) -> T) = apply { this.contextBuilder = contextBuilder }

    fun build() =
        ArgumentPath(
            senderType,
            arguments,
            permissions,
            contextBuilder ?: error("Context builder not set"),
        )
}
