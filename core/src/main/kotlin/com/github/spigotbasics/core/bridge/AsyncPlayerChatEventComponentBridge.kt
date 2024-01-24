package com.github.spigotbasics.core.bridge

import net.kyori.adventure.text.Component
import org.bukkit.event.player.AsyncPlayerChatEvent

object AsyncPlayerChatEventComponentBridge: ComponentBridge<AsyncPlayerChatEvent> {
    override fun apply(theObject: AsyncPlayerChatEvent, component: Component) {
        TODO("Not yet implemented")
    }
}