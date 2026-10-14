package com.github.spigotbasics.modules.basicsinvsee

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import java.util.UUID

class BasicsInvSeeModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.invsee",
            "Allows the player to look into other players' inventories",
        )
    private val permissionEdit =
        permissionManager.createSimplePermission(
            "basics.invsee.edit",
            "Allows the player to modify other players' inventories using /invsee",
        )

    private fun msgInvSee(player: Player) = messages.getMessage("invsee").concerns(player)

    private val readOnlyViewers = mutableSetOf<UUID>()

    override fun onEnable() {
        eventBus.subscribe(InventoryClickEvent::class.java, { cancelIfReadOnly(it) }, EventPriority.LOWEST)
        eventBus.subscribe(InventoryDragEvent::class.java, { cancelIfReadOnly(it) }, EventPriority.LOWEST)
        eventBus.subscribe(InventoryCloseEvent::class.java) { readOnlyViewers.remove(it.player.uniqueId) }

        commandFactory.parsedCommandBuilder("invsee", permission).mapContext {
            usage = "<player>"
            description("Opens another player's inventory")

            path {
                playerOnly()
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
                    val viewer = sender as Player
                    val target = context["player"] as Player
                    viewer.openInventory(target.inventory) ?: return
                    // Opening a new inventory fires a close event for the old one, so mark read-only afterwards
                    if (!viewer.hasPermission(permissionEdit)) readOnlyViewers.add(viewer.uniqueId)
                    msgInvSee(target).sendToPlayer(viewer)
                }
            },
        ).register()
    }

    override fun onDisable() {
        readOnlyViewers.clear()
    }

    private fun cancelIfReadOnly(event: InventoryInteractEvent) {
        if (event.whoClicked.uniqueId in readOnlyViewers) event.isCancelled = true
    }
}
