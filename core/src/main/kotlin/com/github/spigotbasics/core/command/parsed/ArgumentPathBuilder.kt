package com.github.spigotbasics.core.command.parsed

import org.bukkit.permissions.Permission

abstract class ArgumentPathBuilder<T : ParsedCommandContext> {
    protected var senderType: SenderType<*> = AnySender
    protected var arguments = emptyList<Pair<String, CommandArgument<*>>>()
    protected var permissions = emptyList<Permission>()

    private fun senderType(senderType: SenderType<*>) = apply { this.senderType = senderType }

    fun playerOnly() = apply { senderType(PlayerSender) }

    fun permissions(vararg permissions: Permission) = apply { this.permissions = permissions.toList() }

    fun arguments(vararg arguments: Pair<String, CommandArgument<*>>) = apply { this.arguments = arguments.toList() }

    fun arguments(block: ArgumentBuilder.() -> Unit) = apply {
        val argumentBuilder = ArgumentBuilder()
        argumentBuilder.block()
        this.arguments = argumentBuilder.build()
    }

    abstract fun build(): ArgumentPath<T>
}
