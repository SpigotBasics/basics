package com.github.spigotbasics.modules.basicsversion

import cloud.commandframework.Description
import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInfo

class BasicsVersionModule(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

    override fun enable() {
        commandManager.command(
            commandManager.commandBuilder("basics")
                .literal("version", Description.of("Show the version of Basics"))
                .handler { context ->
                    context.sender().sendMessage("Basics version ${plugin.description.version}")
                })
    }

}