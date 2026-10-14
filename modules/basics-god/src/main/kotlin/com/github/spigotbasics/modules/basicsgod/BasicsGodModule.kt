package com.github.spigotbasics.modules.basicsgod

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import com.google.gson.JsonPrimitive
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

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

    private lateinit var storage: NamespacedStorage
    private val godPlayers = ConcurrentHashMap.newKeySet<UUID>()
    private val storedPlayers = ConcurrentHashMap.newKeySet<UUID>() // Players with god mode saved in storage

    override fun onEnable() {
        storage = createStorage()

        eventBus.subscribe(EntityDamageEvent::class.java, ::onDamage, EventPriority.HIGH)
        eventBus.subscribe(FoodLevelChangeEvent::class.java, ::onFoodLevelChange, EventPriority.HIGH)

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

    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> =
        storage.getJsonElement(uuid.toString()).thenAccept { json ->
            if (json != null) {
                storedPlayers += uuid
                godPlayers += uuid
            }
        }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        val god = uuid in godPlayers
        if (god == (uuid in storedPlayers)) return CompletableFuture.completedFuture(null)
        return storage.setJsonElement(uuid.toString(), if (god) JsonPrimitive(true) else null)
    }

    override fun forgetPlayerData(uuid: UUID) {
        godPlayers -= uuid
        storedPlayers -= uuid
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
