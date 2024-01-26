package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.minimessage.TagResolverFactory
import java.io.File

class CoreMessages(file: File, tagResolverFactory: TagResolverFactory): SavedConfig(file, tagResolverFactory) {

    fun noPermission(permission: String): Message {
        return getMessage("no-permission").tags("permission" to permission)
    }

    val commandNotFromConsole: Message
        get() = getMessage("command-not-from-console")

    val mustSpecifyPlayerFromConsole: Message
        get() = getMessage("must-specify-player-from-console")

}