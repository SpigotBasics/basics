package com.github.spigotbasics.modules.basicsbroadcast

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.Bukkit

class BasicsBroadcastModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val commandPerm = permissionManager.createSimplePermission("basics.broadcast", "Allows the user to broadcast messages")
    val parsedPerm = permissionManager.createSimplePermission("basics.broadcast.parsed", "Allows the user to broadcast parsed messages")

    override fun onEnable() {
        createCommand()
            .name("broadcast")
            .permission(commandPerm)
            .description("Broadcasts a message to all players")
            .usage("/broadcast [--parsed] <message>")
            .executor(BroadcastExecutor(this))
            .register()
    }
    
}