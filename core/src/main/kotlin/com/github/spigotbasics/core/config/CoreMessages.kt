package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import org.bukkit.permissions.Permission
import java.io.File

class CoreMessages(file: File, messageFactory: MessageFactory): SavedConfig(file, messageFactory) {

    fun noPermission(permission: Permission): Message {
        return getMessage("no-permission").tags("permission" to permission.name)
    }

    fun noPermission(): Message {
        return getMessage("no-permission")
    }

    fun unknownOption(option: String): Message {
        return getMessage("unknown-option").tagUnparsed("option", option)
    }

    val commandNotFromConsole: Message
        get() = getMessage("command-not-from-console")

    val mustSpecifyPlayerFromConsole: Message
        get() = getMessage("must-specify-player-from-console")

}