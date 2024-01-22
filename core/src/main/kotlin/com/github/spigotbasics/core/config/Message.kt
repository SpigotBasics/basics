package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.extensions.miniComponents
import com.github.spigotbasics.core.extensions.papi
import net.kyori.adventure.audience.Audience
import org.bukkit.OfflinePlayer

/**
 * Represents a message containing of zero or more lines
 *
 * See [SavedConfig.getMessage] on how to obtain a message
 *
 * @property lines Lines of the message
 * @constructor Create new message
 */
data class Message(var lines: List<String>) {
    companion object {
        /**
         * Represents a disabled message that will not do anything when sent
         */
        val DISABLED = Message()
    }

    internal constructor(line: String) : this(listOf(line))
    private constructor() : this(emptyList())

    /**
     * Applies the given transform to each line of the message
     *
     * @param transform Transform to apply
     * @return This message
     */
    fun map(transform: (String) -> String): Message {
        lines = lines.map(transform)
        return this
    }

    /**
     * Applies PlaceholderAPI placeholders to each line of the message
     *
     * @param player Player to apply placeholders for, or null
     * @return This message
     */
    fun papi(player: OfflinePlayer?): Message {
        lines = lines.map { it.papi(player) }
        return this
    }

    /**
     * Parses the message from MiniMessage format to Components and sends them to the given audience
     *
     * @param receiver Audience to send the message to
     */
    fun sendMiniTo(receiver: Audience) {
        lines.forEach { receiver.sendMessage(it.miniComponents()) }
    }

}