package com.github.spigotbasics.core.command.parsed.dsl.argumentpathbuilder

import com.github.spigotbasics.core.command.parsed.AnySender
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.PlayerSender
import com.github.spigotbasics.core.command.parsed.SenderType
import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.command.parsed.context.CommandContext
import com.github.spigotbasics.core.command.parsed.dsl.ArgumentBuilder
import org.bukkit.permissions.Permission

abstract class ArgumentPathBuilder<T : CommandContext> {
    protected var senderType: SenderType<*> = AnySender
    protected var arguments = emptyList<Pair<String, CommandArgument<*>>>()
    protected var permissions = emptyList<Permission>()

    private fun senderType(senderType: SenderType<*>) = apply { this.senderType = senderType }

    fun playerOnly() = apply { senderType(PlayerSender) }

    fun permissions(vararg permissions: Permission) = apply { this.permissions = permissions.toList() }

    fun arguments(vararg arguments: Pair<String, CommandArgument<*>>) = apply { this.arguments = arguments.toList() }

    fun arguments(block: ArgumentBuilder.() -> Unit) =
        apply {
            val argumentBuilder = ArgumentBuilder()
            argumentBuilder.block()
            this.arguments = argumentBuilder.build()
        }

    abstract fun build(): ArgumentPath<T>
}
