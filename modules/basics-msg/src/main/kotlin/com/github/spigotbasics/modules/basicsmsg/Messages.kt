package com.github.spigotbasics.modules.basicsmsg

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig

class Messages(context: ConfigInstantiationContext) : SavedConfig(context) {
    val noRecentMessages get() = getMessage("no-recent-messages")
    val recentNotOnline get() = getMessage("recent-not-online")

    fun formatReceived(message: String) = getMessage("format-received").tagParsed("message", message)

    fun formatSent(message: String) = getMessage("format-sent").tagParsed("message", message)

    fun formatSelf(message: String) = getMessage("format-self").tagParsed("message", message)

    fun formatConsole(message: String) = getMessage("format-console").tagParsed("message", message)

    fun formatOther(message: String) = getMessage("format-other").tagParsed("message", message)
}
