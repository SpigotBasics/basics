package com.github.spigotbasics.core.event.old

import com.github.spigotbasics.core.BasicsPlugin
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import java.util.LinkedList
import java.util.concurrent.ConcurrentHashMap


class BasicsEventBus(private val plugin: BasicsPlugin) : Listener {

    private val handlers: MutableMap<Class<out Event>, List<SubscribedListener<out Event>>> = ConcurrentHashMap()
    private val subscribed: MutableSet<Class<out Event>> = ConcurrentHashMap.newKeySet()

    fun <T : Event> subscribe(eventClass: Class<T>, action: (T) -> Unit) {
        val listener = SubscribedListener(this, action, eventClass)
        (handlers.computeIfAbsent(eventClass) { _ -> LinkedList() } as MutableList).add(
            listener
        )
        ensureSubscribed(eventClass)
    }

    fun <T : Event> subscribe(listener: BasicsListener<T>) {
        subscribe(listener.eventClass, listener::handle)
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
            eventClass, this, EventPriority.NORMAL,
            { _, event ->
                val listeners = handlers[eventClass] ?: return@registerEvent
                for (subscribedListener in listeners) {
                    subscribedListener.call(event)
                }
            }, plugin, true
        )
    }
}
