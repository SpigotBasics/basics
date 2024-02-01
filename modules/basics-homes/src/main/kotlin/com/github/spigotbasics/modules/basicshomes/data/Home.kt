package com.github.spigotbasics.modules.basicshomes.data

import com.github.spigotbasics.core.model.SimpleLocation
import com.github.spigotbasics.core.model.toSimpleLocation
import org.bukkit.Location

data class Home(val name: String, val location: SimpleLocation) {
    constructor(name: String, location: Location) : this(name, location.toSimpleLocation())

}
