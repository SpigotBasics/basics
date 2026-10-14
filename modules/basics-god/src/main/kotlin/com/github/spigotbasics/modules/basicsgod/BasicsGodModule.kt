package com.github.spigotbasics.modules.basicsgod

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class BasicsGodModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.god",
            "Allows the player to toggle god mode",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.god.others",
            "Allows the player to toggle god mode for other players",
        )

    private val msgEnabled
        get() = messages.getMessage("god-enabled")
    private val msgDisabled
        get() = messages.getMessage("god-disabled")

    private fun msgEnabledOthers(player: Player) = messages.getMessage("god-enabled-others").concerns(player)

    private fun msgDisabledOthers(player: Player) = messages.getMessage("god-disabled-others").concerns(player)

    private val godPlayers = mutableSetOf<UUID>()

    override fun onEnable() {
        eventBus.subscribe(EntityDamageEvent::class.java, ::onDamage, EventPriority.HIGH)
        eventBus.subscribe(FoodLevelChangeEvent::class.java, ::onFoodLevelChange, EventPriority.HIGH)
        eventBus.subscribe(PlayerQuitEvent::class.java) { godPlayers.remove(it.player.uniqueId) }

        commandFactory.parsedCommandBuilder("god", permission).mapContext {
            usage = "[player]"
            description("Toggles god mode")

            path {
                playerOnly()
            }

            path {
                permissions(permissionOthers)
                arguments {
                    named("player", SelectorSinglePlayerArg("player"))
                }
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val target = context["player"] as Player? ?: sender as Player
                    val enabled = !godPlayers.remove(target.uniqueId)
                    if (enabled) {
                        godPlayers.add(target.uniqueId)
                        target.fireTicks = 0
                    }

                    val message =
                        when {
                            enabled && sender == target -> msgEnabled
                            !enabled && sender == target -> msgDisabled
                            enabled -> msgEnabledOthers(target)
                            else -> msgDisabledOthers(target)
                        }
                    message.sendToSender(sender)
                }
            },
        ).register()
    }

    override fun onDisable() {
        godPlayers.clear()
    }

    private fun onDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return
        if (player.uniqueId in godPlayers) event.isCancelled = true
    }

    private fun onFoodLevelChange(event: FoodLevelChangeEvent) {
        val player = event.entity as? Player ?: return
        if (player.uniqueId in godPlayers && event.foodLevel < player.foodLevel) event.isCancelled = true
    }
}
