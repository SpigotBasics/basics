package com.github.spigotbasics.core.event

import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import java.util.EnumMap

class BasicsEventBus(private val plugin: Plugin) {

    private val listeners: MutableList<Listener> = ArrayList()
    private val priorityBusses: MutableMap<EventPriority, PriorityEventBus> = EnumMap(EventPriority::class.java);

    fun subscribe(listener: Listener) {
        listeners.add(listener)
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    fun <T : Event> subscribe(eventClass: Class<T>, action: (T) -> Unit) {
        subscribe(eventClass, action, EventPriority.NORMAL)
    }

    fun <T : Event> subscribe(
        eventClass: Class<T>,
        action: (T) -> Unit,
        priority: EventPriority
    ): SubscribedListener<T> {
        val priorityBus = priorityBusses.computeIfAbsent(priority) { PriorityEventBus(plugin, it) }
        return priorityBus.subscribe(eventClass, action)
    }

    fun unsubscribe(listener: Listener) {
        listeners.remove(listener)
        HandlerList.unregisterAll(listener)
    }

    fun unsubscribe(listener: SubscribedListener<*>) {
        val priorityBus = priorityBusses[listener.priority] ?: return
        priorityBus.unsubscribe(listener)
    }

    fun dispose() {
        listeners.forEach(HandlerList::unregisterAll)
        listeners.clear();

        priorityBusses.values.forEach(PriorityEventBus::dispose)
        priorityBusses.clear()
    }
}
