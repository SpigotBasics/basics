package com.github.spigotbasics.modules.basicsversion

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInfo

class BasicsVersionModule(plugin: BasicsPlugin, info: ModuleInfo) : AbstractBasicsModule(plugin, info) {

    override fun enable() {
        commandManager.registerCommand(BasicsVersionCommand(plugin.description))
    }

}
