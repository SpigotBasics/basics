package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.extensions.toShortString
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

class FloatRangeArg(name: String, private val min: () -> Float, private val max: () -> Float) : FloatArg(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Float? {
        val float = super.parse(sender, value)
        return if (float != null && isInRange(float)) float else null
    }

    private fun isInRange(value: Float): Boolean {
        return value in min()..max()
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        val given = super.parse(sender, value) ?: return super.errorMessage(sender, value)
        return Basics.messages.invalidValueForArgumentNumberNotInRange(name, given, min(), max())
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return listOf(min(), max()).map { it.toShortString() }.partialMatches(typing)
    }
}
