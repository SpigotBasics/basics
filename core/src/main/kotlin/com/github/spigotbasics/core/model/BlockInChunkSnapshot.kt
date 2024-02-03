package com.github.spigotbasics.core.model

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import org.bukkit.ChunkSnapshot
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World

data class BlockInChunkSnapshot(
    val x: Int, val y: Int, val z: Int, val chunk: ChunkSnapshot, val minY: Int, val maxY: Int) {
    companion object {
        fun fromLocation(location: Location, chunk: ChunkSnapshot): BlockInChunkSnapshot {

            val world = location.world ?: throw WorldNotLoadedException(location)

            val worldX = location.blockX
            val worldY = location.blockY
            val worldZ = location.blockZ

            val x = worldX and 0xF
            val y = worldY
            val z = worldZ and 0xF

            return BlockInChunkSnapshot(x, y, z, chunk, world.minHeight, world.maxHeight)
        }
    }

    fun toLocation(world: World): Location {

        val worldX = this.chunk.x shl 4 or this.x
        val worldY = this.y
        val worldZ = this.chunk.z shl 4 or this.z

        return Location(world, worldX + .5, worldY.toDouble(), worldZ + .5)
    }

    fun getType(): Material {
        if(y < minY || y >= maxY) {
            return Material.AIR
        }
        return chunk.getBlockType(x, y, z)
    }

    fun getRelativeY(yOffset: Int): BlockInChunkSnapshot {
        return BlockInChunkSnapshot(x, y + yOffset, z, chunk, minY, maxY)
    }
}