package com.github.spigotbasics.core.command.factory

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.command.parsed.context.CommandContext
import com.github.spigotbasics.core.command.parsed.dsl.commandbuilder.GenericContextParsedCommandBuilder
import com.github.spigotbasics.core.command.parsed.dsl.commandbuilder.MapContextParsedCommandBuilder
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
    fun <T : CommandContext> context(): GenericContextParsedCommandBuilder<T> {
        return GenericContextParsedCommandBuilder(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }

    fun <T : CommandContext> context(block: GenericContextParsedCommandBuilder<T>.() -> Unit): GenericContextParsedCommandBuilder<T> {
        val builder =
            GenericContextParsedCommandBuilder<T>(
                coreConfig = coreConfig,
                messageFactory = messageFactory,
                coreMessages = coreMessages,
                commandManager = commandManager,
                name = name,
                permission = permission,
            )
        builder.block() // Apply the DSL configurations
        return builder // return .build() ?
    }

    fun mapContext(): MapContextParsedCommandBuilder {
        return MapContextParsedCommandBuilder(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }

    fun mapContext(block: MapContextParsedCommandBuilder.() -> Unit): MapContextParsedCommandBuilder {
        val builder =
            MapContextParsedCommandBuilder(
                coreConfig = coreConfig,
                messageFactory = messageFactory,
                coreMessages = coreMessages,
                commandManager = commandManager,
                name = name,
                permission = permission,
            )
        builder.block() // Apply the DSL configurations
        return builder // return .build() ?
    }
}
