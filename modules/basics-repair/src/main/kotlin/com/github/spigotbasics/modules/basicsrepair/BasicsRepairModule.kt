package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext

class BasicsRepairModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val msgConfig = getConfig(ConfigName.MESSAGES)

    val msgRepairHandOther: Message
        get() = msgConfig.getMessage("repair-hand-other")

    val msgRepairHandSelf: Message
        get() = msgConfig.getMessage("repair-hand-self")

    val msgRepairAllOther: Message
        get() = msgConfig.getMessage("repair-all-other")

    val msgRepairAllSelf: Message
        get() = msgConfig.getMessage("repair-all-self")

    override fun onEnable() {
        commandManager.registerCommand(RepairCommand(this))
    }

    override fun reloadConfig() {
        super.reloadConfig()
        msgConfig.reload()
    }

}
