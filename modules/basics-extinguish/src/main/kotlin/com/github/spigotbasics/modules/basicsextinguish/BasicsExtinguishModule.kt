package com.github.spigotbasics.modules.basicsextinguish

import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsExtinguishModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permExtinguish =
        permissionManager.createSimplePermission(
            "basics.extinguish",
            "Allows the player to extinguish themself",
        )
    val permExtinguishOthers =
        permissionManager.createSimplePermission(
            "basics.extinguish.others",
            "Allows the player to extinguish others",
        )
    val messageExtinguished = messages.getMessage("extinguished")
    val messageExtinguishedOther = messages.getMessage("extinguished-others")

    override fun onEnable() {
        val playerArg = SelectorSinglePlayerArg("Player")
        commandFactory.parsedCommandBuilder("extinguish", permExtinguish)
            .mapContext {
                usage = "[player]"
                description("Extinguished Players")

                path {
                    playerOnly()
                }

                path {
                    permissions(permExtinguishOthers)
                    arguments {
                        named("player", playerArg)
                    }
                }
            }.executor(BasicsExtinguishExecutor(this)).register()
    }
}
