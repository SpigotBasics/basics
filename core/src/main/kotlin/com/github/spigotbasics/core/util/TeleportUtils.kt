package com.github.spigotbasics.core.util

import com.github.spigotbasics.core.Spiper
import org.bukkit.ChunkSnapshot
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import java.util.concurrent.CompletableFuture
import kotlin.math.abs

object TeleportUtils {

    fun getSafeTeleportLocationAsync(location: Location, maxDistanceXZ: Int): CompletableFuture<Location?> {
        val result = CompletableFuture<Location?>()
        val distanceInChunks = calculateChunksRequired(maxDistanceXZ)

        println("Loading surrounding chunks for safe teleportation...")

        Spiper.getSurroundingChunksAsync(location, distanceInChunks).thenAccept { chunks ->
            println("Surrounding Chunks all loaded, now creating snapshots and spiraling outwards to find a safe location.")
            val snapshots = chunks.mapNotNull { it?.chunkSnapshot }
            println("${snapshots.size} snapshots created.")
            spiralOutwardsAsync(location, maxDistanceXZ, snapshots, result)
        }

        println("Process initiated for finding safe teleportation location.")

        return result
    }

    private fun spiralOutwardsAsync(
        location: Location,
        maxDistance: Int,
        snapshots: List<ChunkSnapshot>,
        result: CompletableFuture<Location?>
    ) {
        val originalX = location.blockX
        val originalZ = location.blockZ
        var dx = 0
        var dz = -1
        val maxI = maxDistance * 2 * maxDistance * 2
        var i = 0
        var x = 0
        var z = 0

        fun checkNext() {
            if (i >= maxI) {
                result.complete(null) // No safe location found within the given distance
                return
            }

            if ((-maxDistance / 2 <= x) && (x <= maxDistance / 2) && (-maxDistance / 2 <= z) && (z <= maxDistance / 2)) {
                val checkingLocation =
                    Location(location.world, (originalX + x).toDouble(), location.y, (originalZ + z).toDouble())
                println("Checking location: $checkingLocation")
                if (isLocationSafeAsync(checkingLocation, snapshots)) {
                    result.complete(checkingLocation)
                    return
                } else {
                    // Spiral outwards logic
                    if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                        val temp = dx
                        dx = -dz
                        dz = temp
                    }
                    x += dx
                    z += dz
                    i++
                    checkNext()
                }
            } else {
                // Spiral outwards logic
                if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                    val temp = dx
                    dx = -dz
                    dz = temp
                }
                x += dx
                z += dz
                i++
                checkNext()
            }
        }

        checkNext()
    }

    private fun isLocationSafeAsync(location: Location, snapshots: List<ChunkSnapshot>): Boolean {
        val chunk =
            snapshots.find { snapshot -> snapshot.x == location.blockX shr 4 && snapshot.z == location.blockZ shr 4 }
        if (chunk == null) {
            println("!!!!! No snapshot found for chunk at location: $location")
            return false
        }


        // Use chunk snapshot to check block types
        val blockX = location.blockX and 0xF
        val blockZ = location.blockZ and 0xF
        val blockY = location.blockY

        return isSafeBasedOnSnapshot(chunk, blockX, blockY, blockZ)
    }

    private fun isSafeBasedOnSnapshot(snapshot: ChunkSnapshot, x: Int, y: Int, z: Int): Boolean {
        val feet = snapshot.getBlockType(x, y - 1, z)
        val body = snapshot.getBlockType(x, y, z)
        val head = snapshot.getBlockType(x, y + 1, z)
        return isLocationSafe(feet, body, head)
    }

    private fun spiralOutwardsAsync(location: Location, maxDistance: Int, result: CompletableFuture<Location?>) {
        val originalX = location.blockX
        val originalZ = location.blockZ
        var dx = 0
        var dz = -1
        val maxI = maxDistance * 2 * maxDistance * 2
        var i = 0
        var x = 0
        var z = 0

        fun checkNext() {
            if (i >= maxI) {
                result.complete(null) // No safe location found within the given distance
                return
            }

            if ((-maxDistance / 2 <= x) && (x <= maxDistance / 2) && (-maxDistance / 2 <= z) && (z <= maxDistance / 2)) {
                val checkingLocation =
                    Location(location.world, (originalX + x).toDouble(), location.y, (originalZ + z).toDouble())
                CompletableFuture.supplyAsync {
                    getSafeTeleportLocation(checkingLocation, maxDistance)
                }.thenAccept { safeLocation ->
                    if (safeLocation != null) {
                        result.complete(safeLocation)
                        return@thenAccept
                    } else {
                        // Spiral outwards logic
                        if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                            val temp = dx
                            dx = -dz
                            dz = temp
                        }
                        x += dx
                        z += dz
                        i++
                        checkNext()
                    }
                }
            } else {
                // Spiral outwards logic
                if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
                    val temp = dx
                    dx = -dz
                    dz = temp
                }
                x += dx
                z += dz
                i++
                checkNext()
            }
        }

        checkNext()
    }

    fun getSafeTeleportLocation(target: Location, maxDistanceXZ: Int): Location? {

        println("GetSafeTeleportLocation @ ${target}")

        // First, try to get a safe location with fixed XZ directly
        getSafeTeleportLocationFixedXZ(target)?.let {
            return it // If a safe location is found, return it
        }

        // If not found, start searching around the target location
        val world = target.world ?: return null
        val startX = target.x.toInt()
        val startZ = target.z.toInt()

        // Iterate in a spiral starting from the target location
        var dx = 0
        var dz = 0
        var stage = 0
        var stageStep = 0

        while (abs(dx) <= maxDistanceXZ && abs(dz) <= maxDistanceXZ) {
            val x = startX + dx
            val z = startZ + dz

            // Check the location at the current offset
            val location = Location(world, x.toDouble(), target.y, z.toDouble())
            getSafeTeleportLocationFixedXZ(location)?.let {
                return it // If a safe location is found, return it
            }

            // Spiral logic to move to the next location
            if (stage % 2 == 0) {
                dx += if (stageStep % 2 == 0) 1 else -1
            } else {
                dz += if (stageStep % 2 == 0) 1 else -1
            }

            // Update stage and step counters
            if ((stage % 2 == 0 && dx == dz) || (stage % 2 == 1 && dx == -dz)) {
                stage++
            }
            stageStep = (stageStep + 1) % 4
        }

        // No safe location found within the maxDistanceXZ
        return null
    }

    fun calculateChunksRequired(maxDistanceXZ: Int): Int {
        // Each chunk is 16 blocks in X and Z.
        // Add 15 to maxDistanceXZ to ensure covering the worst case (being at the far edge of a chunk).
        // Divide by 16 (size of a chunk) to get the distance in chunks.
        // Add 1 to ensure we include the center chunk itself.
        // Divide by 2 since the calculation gives total span, but we need distance from center to edge.
        val chunksDistance = ((maxDistanceXZ + 15) / 16) + 1

        return chunksDistance
    }

    fun getSafeTeleportLocationFixedXZ(target: Location): Location? {

        println("GetSafeTeleportLocationFixedXZ @ ${target}")

        // Ensure the location is at a full block position to prevent glitching into blocks
        val fixedLocation = normalizeLocation(target)

        val world = fixedLocation.world ?: return null
        val startY = fixedLocation.y.toInt()
        val lowestY = startY.coerceAtLeast(world.minHeight)
        val highestY = startY.coerceAtMost(world.maxHeight)
        val x = fixedLocation.x
        val z = fixedLocation.z

        for (y in lowestY..world.maxHeight) {
            val location = Location(world, x, y.toDouble(), z)
            if (isLocationSafe(location)) {
                return location
            }
        }

        for (y in highestY downTo world.minHeight) {
            val location = Location(world, x, y.toDouble(), z)
            if (isLocationSafe(location)) {
                return location
            }
        }

        return null
    }

    fun normalizeLocation(location: Location): Location = Location(
        location.world,
        location.x.toInt() + .5,
        location.y.toInt().toDouble(),
        location.z.toInt() + .5
    )


    fun isLocationSafe(location: Location): Boolean {
        val feet = location.clone().add(0.0, -1.0, 0.0).block.type
        val body = location.block.type
        val head = location.clone().add(0.0, 1.0, 0.0).block.type
        return isLocationSafe(feet, body, head/*, location*/)
    }

    fun isLocationSafe(feet: Material, body: Material, head: Material/*normalizedLocation: Location*/): Boolean {
        //val world = normalizedLocation.world ?: return false

        // Block at the player's feet must be solid
        //val feet: Block = world.getBlockAt(normalizedLocation.clone().add(0.0, -1.0, 0.0))
        if (!isSafeToStandOn(feet)) {
            return false
        }

        // Block for the player's body must not be solid
        //val body: Block = world.getBlockAt(normalizedLocation)
        if (!isSafeToStandInside(body)) {
            return false
        }

        // Block for the player's head must not be solid
        //val head: Block = world.getBlockAt(normalizedLocation.clone().add(0.0, 1.0, 0.0))
        if (!isSafeToStandInside(head)) {
            return false
        }

        // Check if the block under the feet is hazardous
        //val underFeet: Block = world.getBlockAt(normalizedLocation.clone().subtract(0.0, 1.0, 0.0))


        // Location is considered safe
        return true
    }

    fun isSafeToStandInside(material: Material): Boolean {
        return material.isAir || material == Material.WATER
    }

    fun isSafeToStandOn(material: Material): Boolean {
        return material.isSolid && !(material == Material.LAVA
                || material == Material.MAGMA_BLOCK
                || material == Material.FIRE
                || material == Material.CAMPFIRE
                || material == Material.SOUL_CAMPFIRE)
    }

    fun getCoordinateTranslationFactor(from: World, to: World): Double {
        val fromEnv = from.environment
        val toEnv = to.environment
        return when {
            fromEnv == World.Environment.NETHER && toEnv != World.Environment.NETHER -> 8.0
            fromEnv != World.Environment.NETHER && toEnv == World.Environment.NETHER -> 0.125
            else -> 1.0
        }
    }

    fun getScaledLocationInOtherWorld(origin: Location, newWorld: World): Location {
        val factor = getCoordinateTranslationFactor(origin.world!!, newWorld)
        return Location(newWorld, origin.x * factor, origin.y + .5, origin.z * factor, origin.yaw, origin.pitch)
    }

}