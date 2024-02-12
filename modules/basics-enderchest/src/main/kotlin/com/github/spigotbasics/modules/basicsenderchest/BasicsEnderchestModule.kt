package com.github.spigotbasics.modules.basicsenderchest

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsEnderchestModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission("basics.enderchest", "Allows opening your enderchest with /enderchest")

    val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.enderchest.others",
            "Allows opening other players enderchests with /enderchest <player>",
        )

    val inventoryTitle get() = messages.getMessage("inventory-title")

    override fun onEnable() {
        commandFactory.rawCommandBuilder("enderchest", permission)
            .description("Opens your enderchest")
            .usage("[player]")
            .executor(EnderchestCommand(this))
            .register()
    }
}
