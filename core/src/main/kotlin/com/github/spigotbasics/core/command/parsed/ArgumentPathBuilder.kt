package com.github.spigotbasics.core.command.parsed

import org.bukkit.permissions.Permission

interface ArgumentPathBuilder<T : ParsedCommandContext> {
    fun senderType(senderType: SenderType<*>): ArgumentPathBuilder<T>

    fun playerOnly(): ArgumentPathBuilder<T> = apply { senderType(PlayerSender) }

    fun permissions(vararg permissions: Permission): ArgumentPathBuilder<T>

    fun arguments(vararg arguments: Pair<String, CommandArgument<*>>): ArgumentPathBuilder<T>

    fun build(): ArgumentPath<T>
}
