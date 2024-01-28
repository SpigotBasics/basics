package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.command.BasicsCommand
import com.github.spigotbasics.core.command.CommandInfo
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsMinimessageModule(val context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {
        val executor = MiniMessageExecutor(messageFactory)
        val commandInfo = CommandInfo.Builder(context.plugin.messages).name("broadcast")
            .permission("basics.command.broadcast")
            .usage("/broadcast <message>")
            .executor(executor)
            .build()
        val command = BasicsCommand(commandInfo)
        commandManager.registerCommand(command)
    }
    
}