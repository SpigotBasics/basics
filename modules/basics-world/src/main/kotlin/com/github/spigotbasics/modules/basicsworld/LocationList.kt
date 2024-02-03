package com.github.spigotbasics.modules.basicsworld

import com.github.spigotbasics.core.model.SimpleLocation
import com.github.spigotbasics.core.model.toLocation
import org.bukkit.Location
import org.bukkit.World

class LocationList {
    val lastLocations = mutableListOf<SimpleLocation>()

    fun storeLastLocation(loc: SimpleLocation) {
        removeLastLocation(loc.world)
        lastLocations.add(loc)
    }

    fun removeLastLocation(world: String) {
        lastLocations.removeIf { it.world == world }
    }

    fun getLastLocation(world: String): SimpleLocation? {
        return lastLocations.find { it.world == world }
    }

    fun getLastLocationOrDefault(world: World): Location {
        return getLastLocation(world.name)?.toLocation(world) ?: world.spawnLocation
    }
}
