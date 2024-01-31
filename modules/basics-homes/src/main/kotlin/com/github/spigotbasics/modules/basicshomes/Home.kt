package com.github.spigotbasics.modules.basicshomes

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

data class Home(val worldName: String, val x: Double, val y: Double, val z: Double, val yaw: Float, val pitch: Float) {
    constructor(location: Location) : this(
        location.world?.name ?: error("Location must have a world"),
        location.x,
        location.y,
        location.z,
        location.yaw,
        location.pitch
    )

    fun toLocation(): Location {
        return Location(getWorld() ?: error("World $worldName does not exist or is not loaded"), x, y, z, yaw, pitch)
    }

    fun getWorld(): World? {
        return Bukkit.getWorld(worldName)
    }

}
