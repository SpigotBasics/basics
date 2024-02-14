package com.github.spigotbasics.core.command.parsed

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class MapContextParsedCommandBuilder(
    coreConfig: CoreConfig,
    messageFactory: MessageFactory,
    coreMessages: CoreMessages,
    commandManager: BasicsCommandManager,
    name: String,
    permission: Permission,
) : ParsedCommandBuilder<MapCommandContext>(
        coreConfig = coreConfig,
        messageFactory = messageFactory,
        coreMessages = coreMessages,
        commandManager = commandManager,
        name = name,
        permission = permission,
    ) {
    fun MapContextParsedCommandBuilder.path(block: MapArgumentPathBuilder.() -> Unit): ArgumentPath<MapCommandContext> {
        val builder = MapArgumentPathBuilder()
        builder.block()
        val built = builder.build()
        this.argumentPaths.add(built)
        return built
    }
}
