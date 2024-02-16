package com.github.spigotbasics.core.command.parsed.dsl.commandbuilder

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.command.parsed.dsl.argumentpathbuilder.MapArgumentPathBuilder
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
) : ParsedCommandBuilder<MapContext>(
        coreConfig = coreConfig,
        messageFactory = messageFactory,
        coreMessages = coreMessages,
        commandManager = commandManager,
        name = name,
        permission = permission,
    ) {
    fun MapContextParsedCommandBuilder.path(block: MapArgumentPathBuilder.() -> Unit): ArgumentPath<MapContext> {
        val builder = MapArgumentPathBuilder().apply(block)
        return builder.build().also(this.argumentPaths::add)
    }
}
