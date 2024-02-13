package com.github.spigotbasics.core.command.parsed

import org.bukkit.permissions.Permission

class MapArgumentPathBuilder : ArgumentPathBuilder<MapCommandContext> {
    private var senderType: SenderType<*> = AnySender
    private var arguments = emptyList<Pair<String, CommandArgument<*>>>()
    private var permissions = emptyList<Permission>()
    // private var contextBuilder: ((Map<String, Any?>) -> T)? = null

    override fun senderType(senderType: SenderType<*>) = apply { this.senderType = senderType }

    override fun playerOnly() = apply { senderType(PlayerSender) }

    override fun permissions(vararg permissions: Permission) = apply { this.permissions = permissions.toList() }

    override fun arguments(vararg arguments: Pair<String, CommandArgument<*>>) = apply { this.arguments = arguments.toList() }

    // fun contextBuilder(contextBuilder: (Map<String, Any?>) -> T) = apply { this.contextBuilder = contextBuilder }

    override fun build() =
        ArgumentPath(
            senderType,
            arguments,
            permissions,
        ) { args -> MapCommandContext(args) }
}
