package com.github.spigotbasics.core.messages.tags.providers

import com.github.spigotbasics.core.extensions.decimals
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import org.bukkit.Location

class LocationTag(private val loc: Location, private val decimalPlaces: Int = 0) : MessageTagProvider {
    override fun getMessageTags(): List<CustomTag> {
        return getMessageTags(decimalPlaces)
    }

    fun getMessageTags(decimalPlaces: Int): List<CustomTag> {
        return listOf(
            CustomTag.parsed("x", loc.x.decimals(decimalPlaces)),
            CustomTag.parsed("y", loc.y.decimals(decimalPlaces)),
            CustomTag.parsed("z", loc.z.decimals(decimalPlaces)),
            CustomTag.parsed("yaw", loc.yaw.decimals(decimalPlaces)),
            CustomTag.parsed("pitch", loc.pitch.decimals(decimalPlaces)),
            CustomTag.parsed("world", loc.world?.name ?: "null"),
        )
    }
}
