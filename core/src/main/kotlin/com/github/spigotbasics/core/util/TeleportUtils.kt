package com.github.spigotbasics.core.util

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.model.BlockInChunkSnapshot
import org.bukkit.*
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate

object TeleportUtils {
    fun findSafeLocationInSameChunkAsync(
        origin: Location,
        minY: Int,
        maxY: Int,
    ): CompletableFuture<Location?> {
        // TODO: Check the surrounding 8 chunks if none was found here.
        return Spiper.getChunkAt(origin).thenApply { chunk ->
            chunk?.chunkSnapshot ?: throw IllegalStateException("Chunk not loaded")
        }
            .thenApplyAsync { snapshot ->
                findSafeLocationInSameChunk(
                    BlockInChunkSnapshot.fromLocation(origin, snapshot),
                    minY,
                    maxY,
                )
            }
            .thenApply { it?.toLocation(origin.world!!) }
    }

    fun findSafeLocationInSameChunk(
        origin: BlockInChunkSnapshot,
        minY: Int,
        maxY: Int,
    ): BlockInChunkSnapshot? {
        var reachedMin = false
        var reachedMax = false
        var step = 0 // To alternate between incrementing and decrementing

        val y =
            if (origin.y in minY..maxY) {
                origin.y
            } else if (origin.y < minY) {
                minY
            } else {
                maxY
            }
        var increment = y
        var decrement = y

        loopInSquare(origin.chunk, origin.x, origin.z, y, minY, maxY, safeSpotPredicate)?.let {
            return it
        }

        while (!reachedMin || !reachedMax) {
            if (step % 2 == 0) { // Even steps: move upwards
                increment++
                if (increment <= maxY && !reachedMax) {
                    // println("Now checking inc Y = $increment")
                    loopInSquare(origin.chunk, origin.x, origin.z, increment, minY, maxY, safeSpotPredicate)?.let {
                        return it
                    }
                    if (increment == maxY) {
                        reachedMax = true
                    }
                }
            } else { // Odd steps: move downwards
                decrement--
                if (decrement >= minY && !reachedMin) {
                    // println("Now checking dec Y = $decrement")
                    loopInSquare(origin.chunk, origin.x, origin.z, decrement, minY, maxY, safeSpotPredicate)?.let {
                        return it
                    }
                    if (decrement == minY) {
                        reachedMin = true
                    }
                }
            }
            step++

            // Break the loop if both minY and maxY are reached or if we are out of bounds
            if ((increment >= maxY && decrement <= minY) || increment < minY || decrement > maxY) {
                break
            }
        }
        return null
    }

    val safeSpotPredicate =
        Predicate<BlockInChunkSnapshot> { block ->

            // println("Checking block: $block")

            val below = block.getRelativeY(-1).getType()
            val pos = block.getType()
            val head = block.getRelativeY(1).getType()

            if (!isSafeToStandOn(below)) {
                return@Predicate false
            }
            if (!isSafeToStandInside(pos)) {
                return@Predicate false
            }
            if (!isSafeToStandInside(head)) {
                return@Predicate false
            }

            return@Predicate true
        }

    fun loopInSquare(
        chunk: ChunkSnapshot,
        startX: Int,
        startZ: Int,
        y: Int,
        minY: Int,
        maxY: Int,
        predicate: Predicate<BlockInChunkSnapshot>,
    ): BlockInChunkSnapshot? {
        val size = 16 // Grid size
        val maxDistance = startX.coerceAtLeast(size - startX - 1).coerceAtLeast(startZ.coerceAtLeast(size - startZ - 1))

        for (distance in 0..maxDistance) {
            for (i in -distance..distance) {
                val x1 = startX + i
                val z1 = startZ + distance
                if (x1 in 0 until size && z1 in 0 until size) {
                    val block = BlockInChunkSnapshot(x1, y, z1, chunk, minY, maxY)
                    if (predicate.test(block)) {
                        return block
                    }
                }

                val x2 = startX + i
                val z2 = startZ - distance
                if (x2 in 0 until size && z2 in 0 until size && distance != 0) { // Avoid double-counting the center

                    val block = BlockInChunkSnapshot(x2, y, z2, chunk, minY, maxY)
                    if (predicate.test(block)) {
                        return block
                    }
                }
            }
            for (i in -(distance - 1)..<distance) {
                val x1 = startX + distance
                val z1 = startZ + i
                if (x1 in 0 until size && z1 in 0 until size) {
                    val block = BlockInChunkSnapshot(x1, y, z1, chunk, minY, maxY)
                    if (predicate.test(block)) {
                        return block
                    }
                }

                val x2 = startX - distance
                val z2 = startZ + i
                if (x2 in 0 until size && z2 in 0 until size && distance != 0) { // Avoid double-counting the center

                    val block = BlockInChunkSnapshot(x2, y, z2, chunk, minY, maxY)
                    if (predicate.test(block)) {
                        return block
                    }
                }
            }
        }
        return null
    }

    fun getCoordinateTranslationFactor(
        from: World,
        to: World,
    ): Double {
        val fromEnv = from.environment
        val toEnv = to.environment
        return when {
            fromEnv == World.Environment.NETHER && toEnv != World.Environment.NETHER -> 8.0
            fromEnv != World.Environment.NETHER && toEnv == World.Environment.NETHER -> 0.125
            else -> 1.0
        }
    }

    fun getScaledLocationInOtherWorld(
        origin: Location,
        newWorld: World,
    ): Location {
        val factor = getCoordinateTranslationFactor(origin.world!!, newWorld)
        return Location(newWorld, origin.x * factor, origin.y + .5, origin.z * factor, origin.yaw, origin.pitch)
    }

    fun normalizeLocation(location: Location): Location =
        Location(
            location.world,
            location.x.toInt() + .5,
            location.y.toInt().toDouble(),
            location.z.toInt() + .5,
        )

    fun isSafeToStandInside(material: Material): Boolean {
//        return material.isAir || material == Material.WATER
        return material != Material.LAVA && material != Material.FIRE && Tag.REPLACEABLE.isTagged(material)
    }

    fun isSafeToStandOn(material: Material): Boolean {
        if (material == Material.LAVA ||
            material == Material.MAGMA_BLOCK ||
            material == Material.FIRE ||
            material == Material.CAMPFIRE ||
            material == Material.SOUL_CAMPFIRE
        ) {
            return false
        }
        return material.isSolid
    }
}
