package com.github.spigotbasics.core.event.old

import org.bukkit.event.Event

interface BasicsListener<T : Event> {

    fun handle(event: T);

    val eventClass: Class<T>
}
