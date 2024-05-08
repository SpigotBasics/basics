package com.github.spigotbasics.modules.basicsbroadcast

import com.github.spigotbasics.core.command.parsed.arguments.AnyStringArg
import com.github.spigotbasics.core.command.parsed.arguments.GreedyStringArg
import com.github.spigotbasics.core.command.parsed.arguments.LiteralArg
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsBroadcastModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val commandPerm = permissionManager.createSimplePermission("basics.broadcast", "Allows the user to broadcast messages")
    private val parsedPerm = permissionManager.createSimplePermission("basics.broadcast.parsed", "Allows the user to broadcast parsed messages")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("broadcast", commandPerm)
            .mapContext {
                usage = "[--parsed] <message>"
                description("Broadcast a message to your server")

                path {
                    arguments {
                        named("message", GreedyStringArg("message"))
                    }
                }

                path {
                    arguments {
                        permissions(parsedPerm)
                        named("parsed", LiteralArg("--parsed"))
                        named("message", GreedyStringArg("message"))
                    }
                }
            }.executor(BroadcastCommand(this)).register()
    }
}
