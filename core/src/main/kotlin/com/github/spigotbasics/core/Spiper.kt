package com.github.spigotbasics.core

import io.papermc.lib.PaperLib
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause

/**
 * Facade for PaperLib
 */
object Spiper {

    val isPaper = PaperLib.isPaper()

    fun teleportAsync(entity: Entity, location: Location) {
        PaperLib.teleportAsync(entity, location)
    }

    fun teleportAsync(entity: Entity, location: Location, reason: TeleportCause) {
        PaperLib.teleportAsync(entity, location, reason)
    }

}