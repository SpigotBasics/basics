package com.github.spigotbasics.core.command.factory

import com.github.spigotbasics.core.command.BasicsCommand
import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.command.ParsedCommandBuilder
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.MapCommandContext
import com.github.spigotbasics.core.command.parsed.ParsedCommandContext
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class ParsedCommandBuilderFactory(
    private val coreConfig: CoreConfig,
    private val messageFactory: MessageFactory,
    private val coreMessages: CoreMessages,
    private val commandManager: BasicsCommandManager,
    private val name: String,
    private val permission: Permission,
) {
    fun <T : ParsedCommandContext> context(): ParsedCommandBuilder<T> {
        return ParsedCommandBuilder(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }

    fun mapContext(): ParsedCommandBuilder<MapCommandContext> {
        return ParsedCommandBuilder(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }

    fun mapContext(block: ParsedCommandBuilder<MapCommandContext>.() -> Unit): BasicsCommand {
        val builder = ParsedCommandBuilder<MapCommandContext>(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
        builder.block() // Apply the DSL configurations
        return builder.build()
    }
}
