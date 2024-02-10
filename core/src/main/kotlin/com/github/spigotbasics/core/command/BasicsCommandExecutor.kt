package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.core.module.BasicsModule

abstract class BasicsCommandExecutor(
    val coreMessages: CoreMessages,
    val messageFactory: MessageFactory,
) : BasicsCommandContextHandler(), BasicsTabCompleter {
    constructor(module: BasicsModule) : this(module.coreMessages, module.messageFactory)

    abstract fun execute(context: BasicsCommandContext): CommandResult?

    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        return null
    }
}
