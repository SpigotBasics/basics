package com.github.spigotbasics.core.model

import org.bukkit.Location
import org.bukkit.World

fun Location.toXYZCoords(): XYZCoords {
    return XYZCoords(x, y, z)
}

data class XYZCoords(val x: Double, val y: Double, val z: Double) {

    fun toLocation(world: World): Location {
        return Location(world, x, y, z)
    }

    fun toSimpleLocation(world: String): SimpleLocation {
        return SimpleLocation(world, x, y, z, 0f, 0f)
    }
}
