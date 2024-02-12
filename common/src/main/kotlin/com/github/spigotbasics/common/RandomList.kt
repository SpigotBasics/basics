package com.github.spigotbasics.common

class RandomList<T>(private val items: List<T>, private val repeatAfterPercentage: Double) : List<T> by items {
    private val recentlyUsed = mutableListOf<T>()

    init {
        if (repeatAfterPercentage !in 0.0..1.0) {
            throw IllegalArgumentException("repeatAfterPercentage must be between 0 and 1")
        }
    }

    fun getRandom(): T? {
        if (items.isEmpty()) return null

        // Calculate maxRecentSize based on the specified percentage and the current size of the items list
        val maxRecentSize = maxOf(1, (items.size * repeatAfterPercentage).toInt())
        val availableItems = items.filterNot { it in recentlyUsed }
        val selectedItem = availableItems.randomOrNull() ?: items.random() // Fallback in case all items are in recentlyUsed

        // Update recentlyUsed with the consideration of the dynamic size
        recentlyUsed.add(selectedItem)
        if (recentlyUsed.size > maxRecentSize) {
            recentlyUsed.removeAt(0) // Remove the oldest element to maintain the size
        }

        return selectedItem
    }
}
