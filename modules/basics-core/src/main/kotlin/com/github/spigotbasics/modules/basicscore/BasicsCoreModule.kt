package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.command.parsed.arguments.IntRangeArg
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.modules.basicscore.commands.PrintPermissionsCommand
import com.github.spigotbasics.modules.basicscore.commands.SetDebugLogLevelCommand
import com.github.spigotbasics.modules.basicscore.commands.ShowTpsCommand
import org.bukkit.permissions.PermissionDefault

class BasicsCoreModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val modulePermission =
        permissionManager.createSimplePermission(
            "basics.admin.module",
            "Allows managing Basics modules",
            PermissionDefault.OP,
        )

    val debugPermission =
        permissionManager.createSimplePermission(
            "basics.admin.debug",
            "Allows debugging Basics",
            PermissionDefault.OP,
        )

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("module", modulePermission)
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

        commandFactory.parsedCommandBuilder("basicsdebug", debugPermission)
            .mapContext {
                usage = "<command>"

                path {
                    arguments {
                        sub("printpermissions")
                    }
                    executor(PrintPermissionsCommand())
                }

                path {
                    arguments {
                        sub("setdebugloglevel")
                        named("level", IntRangeArg("Log Level", { 0 }, { 999_999_999 }))
                    }
                    executor(SetDebugLogLevelCommand())
                }

                path {
                    arguments {
                        sub("tps")
                    }
                    executor(ShowTpsCommand())
                }
            }
            .register()
    }
}
