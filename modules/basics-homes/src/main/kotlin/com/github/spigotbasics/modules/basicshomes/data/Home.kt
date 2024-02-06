package com.github.spigotbasics.modules.basicshomes.data

import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import com.github.spigotbasics.core.model.SimpleLocation
import com.github.spigotbasics.core.model.toSimpleLocation
import org.bukkit.Location

data class Home(val name: String, val location: SimpleLocation) : MessageTagProvider {
    constructor(name: String, location: Location) : this(name, location.toSimpleLocation())

    //@Transient
    override fun getMessageTags() = listOf(
        CustomTag.parsed("home", name),
        CustomTag.parsed("x", location.x.toInt().toString()),
        CustomTag.parsed("y", location.y.toInt().toString()),
        CustomTag.parsed("z", location.z.toInt().toString()),
        CustomTag.parsed("world", location.world)
    )

}
