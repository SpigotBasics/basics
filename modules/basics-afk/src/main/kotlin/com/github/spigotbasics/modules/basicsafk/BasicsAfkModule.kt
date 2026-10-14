package com.github.spigotbasics.modules.basicsafk

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID

class BasicsAfkModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.afk",
            "Allows the player to toggle their AFK status",
        )
    private val permissionOthers =
        permissionManager.createSimplePermission(
            "basics.afk.others",
            "Allows the player to toggle the AFK status of other players",
        )

    private fun msgAfkNow(player: Player) = messages.getMessage("afk-now").concerns(player)

    private fun msgAfkNoLonger(player: Player) = messages.getMessage("afk-no-longer").concerns(player)

    private val afkPlayers = mutableSetOf<UUID>()

    override fun onEnable() {
        eventBus.subscribe(PlayerMoveEvent::class.java, ::onMove, EventPriority.MONITOR)
        eventBus.subscribe(PlayerInteractEvent::class.java, { clearAfk(it.player) }, EventPriority.MONITOR)
        eventBus.subscribe(PlayerCommandPreprocessEvent::class.java, ::onCommand, EventPriority.MONITOR)
        eventBus.subscribe(PlayerQuitEvent::class.java) { afkPlayers.remove(it.player.uniqueId) }

        commandFactory.parsedCommandBuilder("afk", permission).mapContext {
            usage = "[player]"
            description("Toggles AFK status")

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
                    if (target.uniqueId in afkPlayers) clearAfk(target) else setAfk(target)
                }
            },
        ).register()
    }

    override fun onDisable() {
        afkPlayers.clear()
    }

    private fun setAfk(player: Player) {
        if (afkPlayers.add(player.uniqueId)) {
            msgAfkNow(player).sendToAllPlayers()
        }
    }

    private fun clearAfk(player: Player) {
        if (afkPlayers.remove(player.uniqueId)) {
            msgAfkNoLonger(player).sendToAllPlayers()
        }
    }

    private fun onMove(event: PlayerMoveEvent) {
        if (afkPlayers.isEmpty()) return
        val from = event.from
        val to = event.to ?: return
        if (from.x == to.x && from.y == to.y && from.z == to.z) return // Ignore looking around
        clearAfk(event.player)
    }

    private fun onCommand(event: PlayerCommandPreprocessEvent) {
        val label = event.message.removePrefix("/").substringBefore(' ')
        if (label.equals("afk", ignoreCase = true) || label.endsWith(":afk", ignoreCase = true)) return
        clearAfk(event.player)
    }
}
