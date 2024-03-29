package com.github.spigotbasics.core.event

import org.bukkit.event.Event
import org.bukkit.event.EventPriority

class SubscribedListener<T : Event>(
    private val bus: PriorityEventBus,
    private val action: (T) -> Unit,
    val priority: EventPriority,
    val eventClass: Class<T>,
) {
    fun call(event: Event) {
        @Suppress("UNCHECKED_CAST")
        action.invoke(event as T)
    }

    fun dispose() {
        bus.unsubscribe(this)
    }
}
