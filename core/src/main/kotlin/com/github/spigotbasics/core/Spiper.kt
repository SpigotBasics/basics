package com.github.spigotbasics.core

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import io.papermc.lib.PaperLib
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

/**
 * Facade for PaperLib
 */
object Spiper {
    val isPaper = PaperLib.isPaper()

    fun teleportAsync(
        entity: Entity,
        location: Location,
    ): CompletableFuture<Boolean> {
        return PaperLib.teleportAsync(entity, location)
    }

    fun teleportAsync(
        entity: Entity,
        location: Location,
        reason: TeleportCause,
    ) {
        PaperLib.teleportAsync(entity, location, reason)
    }

    fun getChunkAt(location: Location): CompletableFuture<Chunk?> = PaperLib.getChunkAtAsync(location)

    fun getSurroundingChunksAsync(
        location: Location,
        distance: Int,
    ): CompletableFuture<List<Chunk?>> {
        val futures = mutableListOf<CompletableFuture<Chunk?>>()

        // Calculate the range of chunks to load
        val chunkX = location.chunk.x
        val chunkZ = location.chunk.z
        val world = location.world ?: throw WorldNotLoadedException(location)

        for (dx in -distance..distance) {
            for (dz in -distance..distance) {
                // Request each chunk asynchronously and add the future to the list
                val future = PaperLib.getChunkAtAsync(world, chunkX + dx, chunkZ + dz)
                futures.add(future)
            }
        }

        // Use CompletableFuture.allOf to wait for all futures to complete
        return CompletableFuture.allOf(*futures.toTypedArray()).thenApply {
            futures.stream()
                .map { future -> future.join() } // Join each future, effectively waiting for it to complete
                .collect(Collectors.toList<Chunk?>()) // Collect the results into a list
        }
    }
}
