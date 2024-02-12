package com.github.spigotbasics.modules.basicsfly

import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.command.RawCommandContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.entity.Player

class BasicsFlyModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.fly",
            "Allows to toggle fly using /fly",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.fly.others",
            "Allows to toggle fly for other players using /fly",
        )
    private val msgEnabled
        get() = messages.getMessage("fly-enabled")
    private val msgDisabled
        get() = messages.getMessage("fly-disabled")

    private fun msgEnabledOthers(player: Player) = messages.getMessage("fly-enabled-others").concerns(player)

    private fun msgDisabledOthers(player: Player) = messages.getMessage("fly-disabled-others").concerns(player)

    override fun onEnable() {
        createCommand("fly", permission)
            .description("Toggles fly mode")
            .usage("[player]")
            .executor(FlyCommandExecutor(this))
            .register()
    }

    inner class FlyCommandExecutor(private val module: BasicsFlyModule) : BasicsCommandExecutor(module) {
        override fun execute(context: RawCommandContext): CommandResult {
            val player =
                if (context.args.size == 1) {
                    requirePermission(context.sender, module.permissionOthers)
                    requirePlayer(context.args[0])
                } else {
                    requirePlayer(context.sender)
                }

            player.allowFlight = !player.allowFlight

            val message =
                when {
                    player.allowFlight && context.sender == player -> module.msgEnabled
                    !player.allowFlight && context.sender == player -> module.msgDisabled
                    player.allowFlight -> module.msgEnabledOthers(player)
                    else -> module.msgDisabledOthers(player)
                }

            message.sendToSender(context.sender)
            return CommandResult.SUCCESS
        }
    }
}
