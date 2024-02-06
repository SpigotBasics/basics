package com.github.spigotbasics.core.util.`_`
//
//import com.github.spigotbasics.core.Spiper
//import org.bukkit.ChunkSnapshot
//import org.bukkit.Location
//import org.bukkit.Material
//import org.bukkit.World
//import java.util.concurrent.CompletableFuture
//
//object TeleportUtilsWithSnapshots {
//
//    fun getSafeTeleportLocationAsync(location: Location, maxDistanceXZ: Int): CompletableFuture<Location?> {
//        val result = CompletableFuture<Location?>()
//        val distanceInChunks = calculateChunksRequired(maxDistanceXZ)
//
//        val worldMinY = location.world!!.minHeight
//        val worldMaxY = location.world!!.maxHeight
//
//        println("Loading surrounding chunks for safe teleportation...")
//
//        Spiper.getSurroundingChunksAsync(location, distanceInChunks).thenAccept { chunks ->
//            println("Surrounding Chunks all loaded, now creating snapshots and spiraling outwards to find a safe location.")
//            val snapshots = chunks.mapNotNull { it?.chunkSnapshot }
//            println("${snapshots.size} snapshots created.")
//            spiralOutwardsAsync(location, maxDistanceXZ, snapshots, worldMinY, worldMaxY, result)
//        }
//
//        println("Process initiated for finding safe teleportation location.")
//
//        return result
//    }
//
//    private fun spiralOutwardsAsync(
//        location: Location,
//        maxDistance: Int,
//        snapshots: List<ChunkSnapshot>,
//        worldMinY: Int,
//        worldMaxY: Int,
//        result: CompletableFuture<Location?>
//    ) {
//        val originalX = location.blockX
//        val originalZ = location.blockZ
//        var dx = 0
//        var dz = -1
//        val maxI = maxDistance * 2 * maxDistance * 2
//        var i = 0
//        var x = 0
//        var z = 0
//
//        fun checkNext() {
//            if (i >= maxI) {
//                result.complete(null) // No safe location found within the given distance
//                return
//            }
//
//            if ((-maxDistance / 2 <= x) && (x <= maxDistance / 2) && (-maxDistance / 2 <= z) && (z <= maxDistance / 2)) {
//                val checkingLocation = Location(location.world, (originalX + x).toDouble(), location.y, (originalZ + z).toDouble())
//                println("Checking location: $checkingLocation")
//                if (adjustToSafeYLocation(checkingLocation, snapshots, worldMinY, worldMaxY)) {
//                    result.complete(checkingLocation)
//                    return
//                } else {
//                    // Spiral outwards logic
//                    if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
//                        val temp = dx
//                        dx = -dz
//                        dz = temp
//                    }
//                    x += dx
//                    z += dz
//                    i++
//                    checkNext()
//                }
//            } else {
//                // Spiral outwards logic
//                if (x == z || (x < 0 && x == -z) || (x > 0 && x == 1 - z)) {
//                    val temp = dx
//                    dx = -dz
//                    dz = temp
//                }
//                x += dx
//                z += dz
//                i++
//                checkNext()
//            }
//        }
//
//        checkNext()
//    }
//
//    private fun adjustToSafeYLocation(location: Location, snapshots: List<ChunkSnapshot>, worldMinY: Int, worldMaxY: Int): Boolean {
//        val chunk = snapshots.find { snapshot -> snapshot.x == location.blockX shr 4 && snapshot.z == location.blockZ shr 4 }
//        if (chunk == null) {
//            println("!!!!! No snapshot found for chunk at location: $location")
//            return false
//        }
//
//        // Use chunk snapshot to check block types
//        val blockX = location.blockX and 0xF
//        val blockZ = location.blockZ and 0xF
//        val blockY = location.blockY
//        for (y in blockY..worldMaxY) { // Search vertically within the chunk
//            if (isSafeBasedOnSnapshot(chunk, blockX, y, blockZ, worldMinY, worldMaxY)) {
//                location.y = y.toDouble()
//                return true
//            }
//        }
//        for (y in blockY downTo worldMinY) { // Search vertically within the chunk
//            if (isSafeBasedOnSnapshot(chunk, blockX, y, blockZ, worldMinY, worldMaxY)) {
//                location.y = y.toDouble()
//                return true
//            }
//        }
//
//        return false
//    }
//
//    private fun isSafeBasedOnSnapshot(snapshot: ChunkSnapshot, x: Int, y: Int, z: Int, worldMinY: Int, worldMaxY: Int): Boolean {
//        if (y <= worldMinY || y >= worldMaxY - 1) return false // Ensure Y is within world bounds
//
//        val feet = snapshot.getBlockType(x, y - 1, z)
//        val body = snapshot.getBlockType(x, y, z)
//        val head = snapshot.getBlockType(x, y + 1, z)
//        return isLocationSafe(feet, body, head)
//    }
//
//    fun calculateChunksRequired(maxDistanceXZ: Int): Int {
//        val chunksDistance = ((maxDistanceXZ + 15) / 16) + 1
//        return chunksDistance
//    }
//
//    fun isLocationSafe(feet: Material, body: Material, head: Material): Boolean {
//        return isSafeToStandOn(feet) && isSafeToStandInside(body) && isSafeToStandInside(head)
//    }
//
//    fun isSafeToStandInside(material: Material): Boolean {
//        return material.isAir || material == Material.WATER
//    }
//
//    fun isSafeToStandOn(material: Material): Boolean {
//        return material.isSolid && material != Material.LAVA && material != Material.MAGMA_BLOCK && material != Material.FIRE && material != Material.CAMPFIRE && material != Material.SOUL_CAMPFIRE
//    }
//
//    fun getCoordinateTranslationFactor(from: World, to: World): Double {
//        val fromEnv = from.environment
//        val toEnv = to.environment
//        return when {
//            fromEnv == World.Environment.NETHER && toEnv != World.Environment.NETHER -> 8.0
//            fromEnv != World.Environment.NETHER && toEnv == World.Environment.NETHER -> 0.125
//            else -> 1.0
//        }
//    }
//
//    fun getScaledLocationInOtherWorld(location: Location, toWorld: World): Location {
//        val factor = getCoordinateTranslationFactor(location.world!!, toWorld)
//        val newX = location.x * factor
//        val newZ = location.z * factor
//        return Location(toWorld, newX, location.y, newZ, location.yaw, location.pitch)
//    }
//}
