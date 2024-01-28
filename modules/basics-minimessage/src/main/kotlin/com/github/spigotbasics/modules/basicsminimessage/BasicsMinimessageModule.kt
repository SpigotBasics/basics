package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.entity.Player

class BasicsMinimessageModule(val context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {

        // Option 1: Inline the execution logic
        createCommand()
            .name("broadcast1")
            .permission("basics.broadcast")
            .description("Broadcasts a message to all players")
            .usage("/broadcast <message>")
            .executor { context ->

                val text = context.args.joinToString(" ")
                var message = messageFactory.createPlainMessage(text)

                message.sendToAllPlayers()
                true

            }
            .register()

        // Option 2 - separate executor class
        createCommand()
            .name("broadcast2")
            .permission("basics.broadcast")
            .description("Broadcasts a message to all players")
            .usage("/broadcast [--parse] <message>")
            .executor(MiniMessageExecutor(this))
            .register()
    }
    
}