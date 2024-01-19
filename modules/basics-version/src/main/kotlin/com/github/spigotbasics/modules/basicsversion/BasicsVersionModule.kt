package com.github.spigotbasics.modules.basicsversion

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsVersionModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {
        commandManager.registerCommand(BasicsVersionCommand(plugin.description))
    }

}
