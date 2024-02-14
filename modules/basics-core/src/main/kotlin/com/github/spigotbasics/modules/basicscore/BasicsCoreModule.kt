package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.arguments.ModuleArg
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
                        add("sub", literal("help"))
                    }
                }

                // module list
                path {
                    arguments {
                        add("sub", literal("list"))
                    }
                }

                // module info <module>
                path {
                    arguments {
                        add("sub", literal("info"))
                        add("module", ModuleArg.LoadedModules("Module"))
                    }
                }

                // module enable <module>
                path {
                    arguments {
                        add("sub", literal("enable"))
                        add("module", ModuleArg.DisabledModules("Module"))
                    }
                }

                // module disable <module>
                path {
                    arguments {
                        add("sub", literal("disable"))
                        add("module", ModuleArg.EnabledModules("Module"))
                    }
                }

                // module reloadjar <module>
                path {
                    arguments {
                        add("sub", literal("reloadjar"))
                        add("module", ModuleArg.LoadedModules("Module"))
                    }
                }

                // module reload <module>
                path {
                    arguments {
                        add("sub", literal("reload"))
                        add("module", ModuleArg.EnabledModules("Module"))
                    }
                }

                // module unload <module>
                path {
                    arguments {
                        add("sub", literal("unload"))
                        add("module", ModuleArg.LoadedModules("Module"))
                    }
                }

                // module load <module>
                path {
                    arguments {
                        add("sub", literal("load"))
                        add("moduleFileName", UnloadedModuleFileArg("Module File", plugin.moduleManager))
                    }
                }

                // no arguments -> help
                path {
                }
            }
            .executor(NewModulesCommand(this))
            .register()
    }
}
