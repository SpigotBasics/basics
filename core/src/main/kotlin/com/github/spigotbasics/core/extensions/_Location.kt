package com.github.spigotbasics.core.extensions

import com.github.spigotbasics.common.SimpleLocation
import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import org.bukkit.Bukkit
import org.bukkit.Location

@Throws(WorldNotLoadedException::class)
fun Location.toSimpleLocation(): SimpleLocation {
    val world = world ?: throw WorldNotLoadedException(this)
    return SimpleLocation(world.name, x, y, z, yaw, pitch)
}

@Throws(WorldNotLoadedException::class)
fun SimpleLocation.toLocation(): Location {
    val world = Bukkit.getWorld(this.world) ?: throw WorldNotLoadedException(this.world)
    return Location(world, x, y, z, yaw, pitch)
}