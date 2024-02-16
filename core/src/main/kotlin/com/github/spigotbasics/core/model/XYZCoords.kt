package com.github.spigotbasics.core.model

import com.github.spigotbasics.core.extensions.decimals
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import org.bukkit.Location
import org.bukkit.World

fun Location.toXYZCoords(): XYZCoords {
    return XYZCoords(x, y, z)
}

data class XYZCoords(val x: Double, val y: Double, val z: Double) : MessageTagProvider {
    fun toLocation(world: World): Location {
        return Location(world, x, y, z)
    }

    fun toSimpleLocation(world: String): SimpleLocation {
        return SimpleLocation(world, x, y, z, 0f, 0f)
    }

    override fun getMessageTags(): List<CustomTag> {
        return getMessageTags(0)
    }

    fun getMessageTags(decimalPlaces: Int): List<CustomTag> {
        return listOf(
            CustomTag.parsed("x", x.decimals(decimalPlaces)),
            CustomTag.parsed("y", y.decimals(decimalPlaces)),
            CustomTag.parsed("z", z.decimals(decimalPlaces)),
        )
    }
}
