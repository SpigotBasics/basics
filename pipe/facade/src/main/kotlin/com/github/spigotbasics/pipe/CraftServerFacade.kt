package com.github.spigotbasics.pipe

import org.bukkit.Bukkit

object CraftServerFacade {
    private val syncCommandsMethod =
        try {
            Bukkit.getServer().javaClass.getDeclaredMethod("syncCommands")
        } catch (_: Exception) {
            null
        }

    fun syncCommands() {
        syncCommandsMethod?.invoke(Bukkit.getServer())
    }
}
