package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message
import org.bukkit.command.CommandSender

class SubcommandArg(name: String) : LiteralArg(name) {
    override fun errorMessage(sender: CommandSender, value: String): Message {
        return if (value != name) {
            Basics.messages.invalidSubcommand(value)
        } else {
            super.errorMessage(sender, value)
        }
    }
}
