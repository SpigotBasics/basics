package com.github.spigotbasics.core.model

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import com.github.spigotbasics.core.extensions.decimals
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
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
    val pitch: Float,
) : MessageTagProvider {
    /**
     * Turns a [SimpleLocation] into a [Location].
     *
     * @return The [Location] representation of the [SimpleLocation].
     * @throws WorldNotLoadedException If the world is not loaded.
     */
    @Throws(WorldNotLoadedException::class)
    fun toLocation(): Location {
        val world = Bukkit.getWorld(this.world) ?: throw WorldNotLoadedException(this.world)
        return Location(world, x, y, z, yaw, pitch)
    }

    /**
     * Turns a [SimpleLocation] into a [Location] for the given world.
     *
     * @param world The world to use.
     * @return The [Location] representation of the [SimpleLocation].
     */
    fun toLocation(world: World): Location {
        return Location(world, x, y, z, yaw, pitch)
    }

    fun toXYZCoords(): XYZCoords {
        return XYZCoords(x, y, z)
    }

    override fun getMessageTags(): List<CustomTag> {
        return getMessageTags(0)
    }

    fun getMessageTags(decimalPlaces: Int): List<CustomTag> {
        return listOf(
            CustomTag.parsed("x", x.decimals(decimalPlaces)),
            CustomTag.parsed("y", y.decimals(decimalPlaces)),
            CustomTag.parsed("z", z.decimals(decimalPlaces)),
            CustomTag.parsed("yaw", yaw.decimals(decimalPlaces)),
            CustomTag.parsed("pitch", pitch.decimals(decimalPlaces)),
            CustomTag.parsed("world", world),
        )
    }
}

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
