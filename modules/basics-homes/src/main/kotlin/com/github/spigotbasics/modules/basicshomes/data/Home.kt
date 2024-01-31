package com.github.spigotbasics.modules.basicshomes.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

data class Home(val name: String, val worldName: String, val x: Double, val y: Double, val z: Double, val yaw: Float, val pitch: Float) {
    constructor(name: String, location: Location) : this(
        name,
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
