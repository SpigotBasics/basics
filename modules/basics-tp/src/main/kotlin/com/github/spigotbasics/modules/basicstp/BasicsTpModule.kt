package com.github.spigotbasics.modules.basicstp

import com.github.spigotbasics.core.command.parsed.arguments.XYZCoordsArg
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsTpModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission = permissionManager.createSimplePermission("basics.tp", "Allows teleporting")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("tp", permission).mapContext {
            usage = "<x y z>"
            path {
                playerOnly()
                arguments {
                    named("coords", XYZCoordsArg("Target Coordinates"))
                }
            }
            executor(TeleportCommand(this@BasicsTpModule))
        }.register()
    }
}
