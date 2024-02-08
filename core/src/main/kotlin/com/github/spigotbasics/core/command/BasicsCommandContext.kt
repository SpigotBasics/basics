package com.github.spigotbasics.core.command

import org.bukkit.Location
import org.bukkit.command.CommandSender

data class BasicsCommandContext(
    val sender: CommandSender,
    val command: BasicsCommand,
    val label: String,
    val args: MutableList<String>,
    val location: Location?,
) {
    private var flagsParsed = false
    val flags: MutableList<String> = mutableListOf()
        get() {
            if (!flagsParsed) throw IllegalStateException("Flags not read yet")
            return field
        }

    fun readFlags(): MutableList<String> {
        flagsParsed = true
        while (args.isNotEmpty() && args[0].startsWith("-")) {
            flags.add(args.removeAt(0).lowercase())
        }
        return flags
    }

    fun popFlag(flag: String): Boolean {
        if (!flagsParsed) throw IllegalStateException("Flags not read yet")
        return flags.remove(flag.lowercase())
    }
}
