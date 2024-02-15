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
        commandFactory.parsedCommandBuilder("module", permission)
            .mapContext {
                usage = "<command> [module]"

                // module help
                path {
                    arguments {
                        sub("help")
                    }
                }

                // module list
                path {
                    arguments {
                        sub("list")
                    }
                }

                // module info <module>
                path {
                    arguments {
                        sub("info")
                        named("module", ModuleArg.LoadedModules("Module"))
                    }
                }

                // module enable <module>
                path {
                    arguments {
                        sub("enable")
                        named("module", ModuleArg.DisabledModules("Module"))
                    }
                }

                // module disable <module>
                path {
                    arguments {
                        sub("disable")
                        named("module", ModuleArg.EnabledModules("Module"))
                    }
                }

                // module reloadjar <module>
                path {
                    arguments {
                        sub("reloadjar")
                        named("module", ModuleArg.LoadedModules("Module"))
                    }
                }

                // module reload <module>
                path {
                    arguments {
                        sub("reload")
                        named("module", ModuleArg.EnabledModules("Module"))
                    }
                }

                // module unload <module>
                path {
                    arguments {
                        sub("unload")
                        named("module", ModuleArg.LoadedModules("Module"))
                    }
                }

                // module load <module>
                path {
                    arguments {
                        sub("loadfile")
                        named("moduleFileName", UnloadedModuleFileArg("Module File", plugin.moduleManager))
                    }
                }

                // no arguments -> help
                path {}
            }
            .executor(ModuleCommand(this))
            .register()
    }
}
