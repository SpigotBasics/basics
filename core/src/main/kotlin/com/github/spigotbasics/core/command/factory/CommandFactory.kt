package com.github.spigotbasics.core.command.factory

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class CommandFactory(
    private val coreConfig: CoreConfig,
    private val messageFactory: MessageFactory,
    private val coreMessages: CoreMessages,
    private val commandManager: BasicsCommandManager,
) {
    @Deprecated("Use ParsedCommandBuilder instead")
    fun rawCommandBuilder(
        name: String,
        permission: Permission,
    ): RawCommandBuilder {
        return RawCommandBuilder(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }

    fun parsedCommandBuilder(
        name: String,
        permission: Permission,
    ): ParsedCommandBuilderFactory {
        return ParsedCommandBuilderFactory(
            coreConfig = coreConfig,
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }
}
