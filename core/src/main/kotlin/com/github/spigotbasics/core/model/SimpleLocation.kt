package com.github.spigotbasics.core.model

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

/**
 * Represents a [Location] that references a [org.bukkit.World] by name.
 *
 * @property world The name of the world.
 * @property x The x coordinate.
 * @property y The y coordinate.
 * @property z The z coordinate.
 * @property yaw The yaw.
 * @property pitch The pitch.
 */
data class SimpleLocation(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float)

/**
 * Turns a [Location] into a [SimpleLocation].
 *
 * @return The [SimpleLocation] representation of the [Location].
 * @throws WorldNotLoadedException If the Location returns a null world.
 */
@Throws(WorldNotLoadedException::class)
fun Location.toSimpleLocation(): SimpleLocation {
    val world = world ?: throw WorldNotLoadedException(this)
    return SimpleLocation(world.name, x, y, z, yaw, pitch)
}

/**
 * Turns a [SimpleLocation] into a [Location].
 *
 * @return The [Location] representation of the [SimpleLocation].
 * @throws WorldNotLoadedException If the world is not loaded.
 */
@Throws(WorldNotLoadedException::class)
fun SimpleLocation.toLocation(): Location {
    val world = Bukkit.getWorld(this.world) ?: throw WorldNotLoadedException(this.world)
    return Location(world, x, y, z, yaw, pitch)
}

/**
 * Turns a [SimpleLocation] into a [Location] for the given world.
 *
 * @param world The world to use.
 * @return The [Location] representation of the [SimpleLocation].
 */
fun SimpleLocation.toLocation(world: World): Location {
    return Location(world, x, y, z, yaw, pitch)
}