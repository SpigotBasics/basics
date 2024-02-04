package com.github.spigotbasics.core.cooldown

import java.util.concurrent.TimeUnit

interface Cooldown<T> {
    fun startCooldown(key: T, duration: Long, unit: TimeUnit = TimeUnit.MILLISECONDS)
    fun startCooldown(key: T)
    fun isOnCooldown(key: T): Boolean
    fun getRemainingTime(key: T, unit: TimeUnit): Long
    fun removeCooldown(key: T)
    fun clear()
    fun clearExpiredEntries()
}