package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.config.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsRepairModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val msgRepairHandOther: Message
        get() = config.getMessage("repair-hand-other")

    val msgRepairHandSelf: Message
        get() = config.getMessage("repair-hand-self")

    val msgRepairAllOther: Message
        get() = config.getMessage("repair-all-other")

    val msgRepairAllSelf: Message
        get()= config.getMessage("repair-all-self")

    override fun onEnable() {
        commandManager.registerCommand(RepairCommand(this))
    }

}
