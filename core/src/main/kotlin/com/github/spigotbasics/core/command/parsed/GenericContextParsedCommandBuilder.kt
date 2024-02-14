package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class GenericContextParsedCommandBuilder<T : ParsedCommandContext>(
    coreConfig: CoreConfig,
    messageFactory: MessageFactory,
    coreMessages: CoreMessages,
    commandManager: BasicsCommandManager,
    name: String,
    permission: Permission,
) : ParsedCommandBuilder<T>(
        coreConfig = coreConfig,
        messageFactory = messageFactory,
        coreMessages = coreMessages,
        commandManager = commandManager,
        name = name,
        permission = permission,
    ) {
    fun GenericContextParsedCommandBuilder<T>.path(block: GenericArgumentPathBuilder<T>.() -> Unit): ArgumentPath<T> {
        val builder = GenericArgumentPathBuilder<T>()
        builder.block()
        val built = builder.build()
        this.argumentPaths.add(built)
        return built
    }
}
