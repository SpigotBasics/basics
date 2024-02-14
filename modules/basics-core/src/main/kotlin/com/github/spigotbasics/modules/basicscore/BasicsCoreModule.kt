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
        commandFactory.parsedCommandBuilder("moduletest", permission)
            .context<TestContext> {
                usage = "<donkey>"

                path {
                    arguments {
                        add("value", literal("donkey"))
                    }
                    contextBuilder { TestContext(it["value"] as String) }
                }
            }
            .executor(TestContextExecutor())
            .register()

        commandFactory.parsedCommandBuilder("module", permission)
            .mapContext {
                usage = "<command> [module]"

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
                        add("module", ModuleArg.LoadedModules("module"))
                    }
                }

                // module enable <module>
                path {
                    arguments {
                        add("sub", literal("enable"))
                        add("module", ModuleArg.DisabledModules("module"))
                    }
                }

                // module disable <module>
                path {
                    arguments {
                        add("sub", literal("disable"))
                        add("module", ModuleArg.EnabledModules("module"))
                    }
                }
            }.executor(NewModulesCommand(plugin.moduleManager, messageFactory))
            .register()
    }
}
