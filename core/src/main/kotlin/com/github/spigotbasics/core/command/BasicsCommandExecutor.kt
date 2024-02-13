package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.command.raw.RawTabCompleter
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule

abstract class BasicsCommandExecutor(
    val coreMessages: CoreMessages,
    val messageFactory: MessageFactory,
) : BasicsCommandContextHandler(), RawTabCompleter {
    constructor(module: BasicsModule) : this(module.coreMessages, module.messageFactory)

    abstract fun execute(context: RawCommandContext): CommandResult?

    override fun tabComplete(context: RawCommandContext): MutableList<String>? {
        return null
    }
}
