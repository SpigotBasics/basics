package com.github.spigotbasics.modules.basicschatformat.data

import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import java.util.UUID

class ChatFormatStore(private val module: BasicsChatFormatModule) {
    val chatFormatData = mutableMapOf<UUID, ChatFormatData>()

    fun setChatData(
        uuid: UUID,
        chatData: ChatFormatData,
    ) {
        this.chatFormatData[uuid] = chatData
    }

    fun getChatDataOrDefault(uuid: UUID) = chatFormatData.getOrDefault(uuid, ChatFormatData(module.messageColor))
}
