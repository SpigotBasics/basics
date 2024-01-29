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

    val permission = permissionManager.createSimplePermission("basics.repair", "Allows to repair your current items")
    val permissionAll = permissionManager.createSimplePermission("basics.repair.all", "Allows to repair all your items")
    val permissionOthers = permissionManager.createSimplePermission("basics.repair.others", "Allows to repair other players' items")

    override fun onEnable() {
        createCommand().name("repair")
            .usage("/repair [--all] [player]")
            .permission(permission)
            .description("Repairs an item")
            .executor(RepairCommand(this))
            .register()
    }

    override fun reloadConfig() {
        super.reloadConfig()
        msgConfig.reload()
    }

}
