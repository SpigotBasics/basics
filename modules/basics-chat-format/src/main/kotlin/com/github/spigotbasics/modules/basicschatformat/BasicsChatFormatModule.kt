package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.bridge.AsyncPlayerChatEventComponentBridge
import com.github.spigotbasics.core.extensions.mini
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.event.player.AsyncPlayerChatEvent

private fun String.escapeFormat(): String {
    return this.replace("%", "%%")
}

class BasicsChatFormatModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    private val format: String
        get() = config.getString("chat-format") ?: "<player-name> <message>"

    override fun onEnable() {
        eventBus.subscribe(AsyncPlayerChatEvent::class.java, this::changeChatFormat)
    }

    fun changeChatFormat(event: AsyncPlayerChatEvent) {
        AsyncPlayerChatEventComponentBridge.apply(event, mini.deserialize(format).escapeFormat() // TODO: Fix this
            .concerns(event.player)
            .build())
    }


    
}