package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.config.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsRepairModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val repairHand: Message
        get() = config.getMessage("repair-hand")

    val repairHandSelf: Message
        get() = config.getMessage("repair-hand-self")

    val repairAll: Message
        get() = config.getMessage("repair-all")

    val repairAllSelf: Message
        get()= config.getMessage("repair-all-self")

    override fun onEnable() {
        val parent: RepairCommand = RepairCommand(this)
        commandManager.registerCommand(parent)
    }

}
