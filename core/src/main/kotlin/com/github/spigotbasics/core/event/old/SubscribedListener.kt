package com.github.spigotbasics.core.event.old

import org.bukkit.event.Event

class SubscribedListener<T : Event>(
    private val bus: BasicsEventBus,
    private val action: (T) -> Unit,
    val eventClass: Class<T>
) {

    fun call(event: Event) {
        @Suppress("UNCHECKED_CAST")
        action.invoke(event as T)
    }

    fun dispose() {
        bus.unsubscribe(this)
    }

}
