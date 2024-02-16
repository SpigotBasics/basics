package com.github.spigotbasics.core.command.parsed.dsl.commandbuilder

import com.github.spigotbasics.core.command.BasicsCommandManager
import com.github.spigotbasics.core.command.parsed.ArgumentPath
import com.github.spigotbasics.core.command.parsed.context.CommandContext
import com.github.spigotbasics.core.command.parsed.dsl.argumentpathbuilder.GenericArgumentPathBuilder
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission

class GenericContextParsedCommandBuilder<T : CommandContext>(
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
        val builder = GenericArgumentPathBuilder<T>().apply(block)
        return builder.build().also(this.argumentPaths::add)
    }
}
