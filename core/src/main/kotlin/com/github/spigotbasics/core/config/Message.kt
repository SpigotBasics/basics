package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.extensions.miniComponents
import com.github.spigotbasics.core.extensions.papi
import net.kyori.adventure.audience.Audience
import org.bukkit.OfflinePlayer

data class Message(var lines: List<String>) {
    companion object {
        val EMPTY = Message()
    }

    constructor(line: String) : this(listOf(line))
    private constructor() : this(emptyList())

    fun map(transform: (String) -> String): Message {
        lines = lines.map(transform)
        return this
    }

    fun papi(player: OfflinePlayer?): Message {
        lines = lines.map { it.papi(player) }
        return this
    }

//    fun mini(player: Audience): List<Component> {
//        return lines.map { it.miniComponents() }
//    }

    fun sendMiniTo(receiver: Audience) {
        lines.forEach { receiver.sendMessage(it.miniComponents()) }
    }

}