package com.github.spigotbasics.core.exceptions

import org.bukkit.Location

class WorldNotLoadedException : IllegalStateException {
    constructor(worldName: String) : super("Given world '$worldName' is not loaded")
    constructor(location: Location) : super("World of location '$location' is not loaded")
}