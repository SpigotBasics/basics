package com.github.spigotbasics.core.bridge

import net.kyori.adventure.text.Component

interface ComponentBridge<T> {

    fun apply(theObject: T, component: Component)

}