package com.github.spigotbasics.modules.basicsmsg

import java.util.UUID

class LastMessagedStore {
    private val lastMessagedStore = mutableMapOf<UUID, UUID>()

    fun setLastMessagedUUID(
        sender: UUID,
        receiver: UUID,
    ) {
        lastMessagedStore[sender] = receiver
        lastMessagedStore[receiver] = sender
    }

    fun getLastMessagedUUID(uuid: UUID): UUID? {
        return lastMessagedStore[uuid]
    }

    fun forgetLastMessagedUUID(uuid: UUID) {
        lastMessagedStore.remove(uuid)
    }
}
