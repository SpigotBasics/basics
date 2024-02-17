package com.github.spigotbasics.modules.basicsextinguish

import com.github.spigotbasics.core.command.common.BasicsCommandExecutor
import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
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
        commandFactory.rawCommandBuilder("extinguish", permExtinguish)
            .description("Extinguishes Players")
            .usage("[player]")
            .aliases(listOf("ext"))
            .executor(ExtinguishExecutor(this))
            .register()
    }

    private inner class ExtinguishExecutor(private val module: BasicsExtinguishModule) : BasicsCommandExecutor(module) {
        override fun execute(context: RawCommandContext): CommandResult {
            val player =
                if (context.args.size == 1) {
                    requirePermission(context.sender, module.permExtinguishOthers)
                    requirePlayer(context.args[0])
                } else {
                    requirePlayer(context.sender)
                }

            player.fireTicks = 0
            val message =
                if (context.sender == player) {
                    module.messageExtinguished
                } else {
                    module.messageExtinguishedOther
                }

            message.concerns(player).sendToSender(context.sender)
            return CommandResult.SUCCESS
        }
    }
}
