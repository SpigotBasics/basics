package com.github.spigotbasics.pipe.paper

import com.github.spigotbasics.pipe.SerializedMiniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage

object NativeComponentConverter {
    private val mini = MiniMessage.miniMessage()

    fun toNativeComponent(serializedMiniMessage: SerializedMiniMessage): Component {
        return mini.deserialize(serializedMiniMessage.value)
    }
}
