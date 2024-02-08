package com.github.spigotbasics.core.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap

class PriorityEventBus(private val plugin: Plugin, private val priority: EventPriority) : Listener {
    private val handlers: MutableMap<Class<out Event>, List<SubscribedListener<out Event>>> = ConcurrentHashMap()
    private val subscribed: MutableSet<Class<out Event>> = ConcurrentHashMap.newKeySet()

    fun <T : Event> subscribe(
        eventClass: Class<T>,
        action: (T) -> Unit,
    ): SubscribedListener<T> {
        val listener = SubscribedListener(this, action, priority, eventClass)
        (handlers.computeIfAbsent(eventClass) { _ -> LinkedList() } as MutableList).add(
            listener,
        )
        ensureSubscribed(eventClass)
        return listener
    }

    fun unsubscribe(listener: SubscribedListener<*>) {
        val eventClass = listener.eventClass
        (handlers[eventClass] as MutableList).remove(listener)
    }

    fun dispose() {
        handlers.clear()
        subscribed.clear()
        HandlerList.unregisterAll(this)
    }

    private fun ensureSubscribed(eventClass: Class<out Event>) {
        if (subscribed.contains(eventClass)) {
            return
        }

        subscribed.add(eventClass)
        Bukkit.getPluginManager().registerEvent(
            eventClass,
            this,
            priority,
            { _, event ->
                val listeners = handlers[eventClass] ?: return@registerEvent
                for (subscribedListener in listeners) {
                    subscribedListener.call(event)
                }
            },
            plugin,
            true,
        )
    }
}
