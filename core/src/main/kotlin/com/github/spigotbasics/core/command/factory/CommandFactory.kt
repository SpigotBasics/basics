package com.github.spigotbasics.core.command.factory

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class CommandFactory(
    private val messageFactory: MessageFactory,
    private val coreMessages: CoreMessages,
    private val commandManager: BasicsCommandManager,
) {
    fun rawCommandBuilder(
        name: String,
        permission: Permission,
    ): RawCommandBuilder {
        return RawCommandBuilder(
            messageFactory = messageFactory,
            coreMessages = coreMessages,
            commandManager = commandManager,
            name = name,
            permission = permission,
        )
    }
}
