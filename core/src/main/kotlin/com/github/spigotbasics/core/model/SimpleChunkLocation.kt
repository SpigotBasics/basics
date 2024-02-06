package com.github.spigotbasics.core.model

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.World

data class SimpleChunkLocation(val world: String, val x: Int, val z: Int) {
    @Throws(WorldNotLoadedException::class)
    fun toChunk(): Chunk {
        val world = Bukkit.getWorld(world) ?: throw WorldNotLoadedException(world)
        return world.getChunkAt(x, z, false)
    }

    @Throws(WorldNotLoadedException::class)
    fun getWorld(): World {
        return Bukkit.getWorld(world) ?: throw WorldNotLoadedException(world)
    }

    companion object {
        fun fromChunk(chunk: Chunk): SimpleChunkLocation {
            return SimpleChunkLocation(chunk.world.name, chunk.x, chunk.z)
        }
    }
}

fun Chunk.toSimpleChunkLocation(): SimpleChunkLocation {
    return SimpleChunkLocation.fromChunk(this)
}
