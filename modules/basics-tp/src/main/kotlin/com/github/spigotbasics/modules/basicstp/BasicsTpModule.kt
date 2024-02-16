package com.github.spigotbasics.modules.basicstp

import com.github.spigotbasics.core.command.parsed.arguments.SelectorMultiPlayerArg
import com.github.spigotbasics.core.command.parsed.arguments.XYZCoordsArg
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsTpModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission = permissionManager.createSimplePermission("basics.tp", "Allows teleporting")
    private val permissionOthers = permissionManager.createSimplePermission("basics.tp.others", "Allows teleporting other players")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("tp", permission).mapContext {
            usage = "[player] <x y z | entity>"

            // x y z
            path {
                playerOnly()
                arguments {
                    named("coords", XYZCoordsArg("Target Coordinates"))
                }
            }

            path {
                arguments {
                    named("player", SelectorMultiPlayerArg("Player"))
                    named("coords", XYZCoordsArg("Target Coordinates"))
                }
                permissions(permissionOthers)
            }

            executor(TeleportCommand(this@BasicsTpModule))
        }.register()
    }
}
