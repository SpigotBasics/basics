package com.github.spigotbasics.modules.basicshomes.data

import com.github.spigotbasics.common.SimpleLocation
import com.github.spigotbasics.core.extensions.toSimpleLocation
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World

data class Home(val name: String, val location: SimpleLocation) {
    constructor(name: String, location: Location) : this(name, location.toSimpleLocation())

}
