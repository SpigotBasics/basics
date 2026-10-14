package com.github.spigotbasics.modules.basicsback

import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import com.github.spigotbasics.core.model.SimpleLocation
import com.github.spigotbasics.core.model.toSimpleLocation
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap

class BasicsBackModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.back",
            "Allows the player to teleport back to their previous location",
        )
    private val permissionOnDeath =
        permissionManager.createSimplePermission(
            "basics.back.ondeath",
            "Allows the player to use /back to return to where they died",
        )

    private val msgBack
        get() = messages.getMessage("back")
    private val msgNoLocation
        get() = messages.getMessage("back-no-location")

    private lateinit var storage: NamespacedStorage
    private val lastLocations = ConcurrentHashMap<UUID, SimpleLocation>()

    private val recordedCauses = setOf(TeleportCause.COMMAND, TeleportCause.PLUGIN)

    override fun onEnable() {
        storage = createStorage()

        eventBus.subscribe(PlayerTeleportEvent::class.java, ::onTeleport, EventPriority.MONITOR)
        eventBus.subscribe(PlayerDeathEvent::class.java, ::onDeath, EventPriority.MONITOR)

        commandFactory.parsedCommandBuilder("back", permission).mapContext {
            description("Teleports you to your previous location")

            path {
                playerOnly()
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val player = sender as Player
                    val location = lastLocations[player.uniqueId]
                    if (location == null) {
                        msgNoLocation.sendToPlayer(player)
                        return
                    }
                    try {
                        Spiper.teleportAsync(player, location.toLocation(), TeleportCause.COMMAND)
                        msgBack.sendToPlayer(player)
                    } catch (e: WorldNotLoadedException) {
                        coreMessages.worldNotLoaded(location.world).sendToPlayer(player)
                    }
                }
            },
        ).register()
    }

    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> =
        storage.getJsonElement(uuid.toString()).thenAccept { json ->
            json?.let { lastLocations[uuid] = Serialization.fromJson(it, SimpleLocation::class.java) }
        }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        val location = lastLocations[uuid] ?: return CompletableFuture.completedFuture(null)
        return storage.setJsonElement(uuid.toString(), Serialization.toJson(location))
    }

    override fun forgetPlayerData(uuid: UUID) {
        lastLocations.remove(uuid)
    }

    private fun onTeleport(event: PlayerTeleportEvent) {
        if (event.cause !in recordedCauses) return
        lastLocations[event.player.uniqueId] = event.from.toSimpleLocation()
    }

    private fun onDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (!player.hasPermission(permissionOnDeath)) return
        lastLocations[player.uniqueId] = player.location.toSimpleLocation()
    }
}
