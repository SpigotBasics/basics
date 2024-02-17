package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

class IntRangeArg(name: String, private val min: () -> Int, private val max: () -> Int) : IntArg(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Int? {
        val int = super.parse(sender, value)
        return if (int != null && isInRange(int)) int else null
    }

    private fun isInRange(value: Int): Boolean {
        return value in min()..max()
    }

    override fun errorMessage(
        sender: CommandSender,
        value: String,
    ): Message {
        val given = value.toIntOrNull() ?: return Basics.messages.invalidValueForArgumentMustBeInteger(name, value)
        return Basics.messages.invalidValueForArgumentNumberNotInRange(name, given, min(), max())
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return listOf(min().toString(), max().toString()).partialMatches(typing)
    }
}
