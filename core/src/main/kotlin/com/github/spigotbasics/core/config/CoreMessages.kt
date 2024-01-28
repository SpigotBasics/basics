package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.MessageFactory
import java.io.File

class CoreMessages(file: File, messageFactory: MessageFactory): SavedConfig(file, messageFactory) {

    fun noPermission(permission: String): Message {
        return getMessage("no-permission").tags("permission" to permission)
    }

    fun noPermission(): Message {
        return getMessage("no-permission")
    }

    val commandNotFromConsole: Message
        get() = getMessage("command-not-from-console")

    val mustSpecifyPlayerFromConsole: Message
        get() = getMessage("must-specify-player-from-console")

}