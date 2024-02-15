package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.messages.Message

class SubcommandArg(name: String) : LiteralArg(name) {
    override fun errorMessage(value: String?): Message {
        return if(value != name) {
            Basics.messages.invalidSubcommand(value ?: "null")
        } else {
            super.errorMessage(value)
        }
    }
}
