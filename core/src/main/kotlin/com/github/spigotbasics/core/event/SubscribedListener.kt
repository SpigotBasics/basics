package com.github.spigotbasics.core.event

import org.bukkit.event.Event
import org.bukkit.event.EventPriority

class SubscribedListener<T : Event>(
    private val bus: PriorityEventBus,
    private val action: (T) -> Unit,
    val priorty: EventPriority,
    val eventClass: Class<T>
) {

    fun call(event: Event) {
        action.invoke(event as T)
    }

    fun dispose() {
        bus.unsubscribe(this)
    }

}
