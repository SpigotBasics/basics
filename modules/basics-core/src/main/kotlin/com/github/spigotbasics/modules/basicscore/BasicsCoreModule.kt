package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.permissions.PermissionDefault

class BasicsCoreModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission(
            "basics.admin.module",
            "Allows managing Basics modules",
            PermissionDefault.OP,
        )

    override fun onEnable() {


        commandFactory.parsedCommandBuilder("module", permission).mapContext {
            usage = "<command> [module]"

            path {
                arguments {
                    add("sub", literal("list"))
                }
            }

            path {
                arguments {
                    add("sub", literal("info"))
                    add("module", ModuleArg)
                }
            }



        }
    }
}
