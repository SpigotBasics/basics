package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.config.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsRepairModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val repairHand: Message = config.getMessage("repair-hand")
    val repairHandSelf: Message = config.getMessage("repair-hand-self")
    val repairAll: Message = config.getMessage("repair-all")
    val repairAllSelf: Message = config.getMessage("repair-all-self")

    override fun onEnable() {
        val parent: RepairCommand = RepairCommand(this)
        commandManager.registerCommand(parent)
        commandManager.registerCommand(FixCommand(parent))
        commandManager.registerCommand(FixAllCommand(parent))
    }

}
