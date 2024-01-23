package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsMinimessageModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    override fun onEnable() {
        commandManager.registerCommand(MiniMessageCommand(tagResolverFactory, audience))
    }
    
}