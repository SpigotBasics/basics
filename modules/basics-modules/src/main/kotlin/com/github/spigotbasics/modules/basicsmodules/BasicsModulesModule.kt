package com.github.spigotbasics.modules.basicsmodules

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInfo

class BasicsModulesModule(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

    override fun enable() {
        commandManager.command(
            commandManager.commandBuilder("basics")
                .literal("modules")
                .literal("list")
                .handler { context ->
                    context.sender().sendMessage("Enabled modules:")
                    plugin.moduleManager.enabledModules.forEach { module ->
                        context.sender().sendMessage(" - ${module.info.name} (${module.info.version})")
                    }
                }
        )
    }
    
}