package com.github.spigotbasics.modules.basicsmsg

import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsMsgModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val formatReceived: Message
        get() = config.getMessage("format-received")

    val formatSent: Message
        get() = config.getMessage("format-sent")

    val formatSelf: Message
        get() = config.getMessage("format-self")

    val formatConsole: Message
        get() = config.getMessage("format-console")

    override fun onEnable() {
        commandManager.registerCommand(MsgCommand(this))
    }
    
}