package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.permissions.PermissionDefault

class BasicsCoreModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val permission = permissionManager.createSimplePermission(
        "basics.admin.module",
        "Allows managing Basics modules",
        PermissionDefault.OP
    )

    override fun onEnable() {
        createCommand().name("module").permission(permission).usage("/module [command]").executor(ModulesCommand(this)).register()
    }

}