package com.github.spigotbasics.core.cooldown

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

object CooldownFactory {
    fun players(): Cooldown<Player> = SimpleCooldown({ it.uniqueId }, 0, TimeUnit.SECONDS)
    fun entities(): Cooldown<Entity> = SimpleCooldown({ it.uniqueId }, 0, TimeUnit.SECONDS)
    fun <T> simple(): Cooldown<T> = SimpleCooldown({ it }, 0, TimeUnit.SECONDS)

}