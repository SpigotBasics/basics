package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
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

    override fun errorMessage(sender: CommandSender, value: String): Message {
        return Basics.messages.invalidValueForArgumentNumberNotInRange(name, value.toIntOrNull() ?: 0, min(), max())
    }
}
