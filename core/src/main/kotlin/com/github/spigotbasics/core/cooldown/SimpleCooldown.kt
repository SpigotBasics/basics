package com.github.spigotbasics.core.cooldown

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class SimpleCooldown<T, Z>(
    private val mapper: (T) -> Z,
    private val defaultDuration: Long = INFINITE,
    private val defaultUnit: TimeUnit = TimeUnit.MILLISECONDS
) : Cooldown<T> {

    companion object {
        const val EXPIRED = 0L
        const val INFINITE = Long.MAX_VALUE
    }

    private val cooldowns = ConcurrentHashMap<Z, Long>()

    override fun startCooldown(key: T, duration: Long, unit: TimeUnit) {
        val mappedKey = mapper(key)
        val expiryTime = if (duration == INFINITE)
            INFINITE
        else
            System.currentTimeMillis() + unit.toMillis(duration)
        cooldowns[mappedKey] = expiryTime
    }

    override fun startCooldown(key: T) {
        startCooldown(key, defaultDuration, defaultUnit)
    }

    private fun getAndRemoveIfExpired(key: Z): Long {
        val currentTime = System.currentTimeMillis()
        return cooldowns[key]?.let { expiryTime ->
            if (expiryTime <= currentTime) {
                cooldowns.remove(key)
                EXPIRED
            } else {
                expiryTime
            }
        } ?: EXPIRED
    }

    override fun isOnCooldown(key: T): Boolean {
        val mappedKey = mapper(key)
        return getAndRemoveIfExpired(mappedKey) != EXPIRED
    }

    override fun getRemainingTime(key: T, unit: TimeUnit): Long {
        val mappedKey = mapper(key)
        val expiryTime = getAndRemoveIfExpired(mappedKey)
        return if (expiryTime == EXPIRED) 0L else unit.convert(
            expiryTime - System.currentTimeMillis(),
            TimeUnit.MILLISECONDS
        )
    }

    override fun removeCooldown(key: T) {
        val mappedKey = mapper(key)
        cooldowns.remove(mappedKey)
    }

    override fun clear() {
        cooldowns.clear()
    }

    override fun clearExpiredEntries() {
        val currentTime = System.currentTimeMillis()
        cooldowns.entries.removeIf { entry -> entry.value <= currentTime }
    }
}