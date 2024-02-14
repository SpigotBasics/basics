package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message

class IntRangeArg(name: String, private val min: () -> Int, private val max: () -> Int) : IntArg(name) {
    override fun parse(value: String): Int? {
        val int = super.parse(value)
        return if (int != null && isInRange(int)) int else null
    }

    private fun isInRange(value: Int): Boolean {
        return value in min()..max()
    }

    override fun errorMessage(value: String?): Message {
        return Basics.messages.invalidValueForArgumentNumberNotInRange(getArgumentName(), value?.toIntOrNull() ?: 0, min(), max())
    }
}
