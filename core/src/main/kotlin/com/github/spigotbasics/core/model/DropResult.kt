package com.github.spigotbasics.core.model

import org.bukkit.Location
import org.bukkit.entity.Item

/**
 * Represents the result of a [drop operation][com.github.spigotbasics.core.extensions.addOrDrop].
 *
 * @property addedToInv Amount of items that were added to the inventory
 * @property droppedToWorld Amount of items that were dropped to the world because the inventory was full
 * @property dropLocation The location where the items were dropped. Will be null if all items were added to the inventory
 * @property droppedItems The items that were dropped to the world
 */
data class DropResult(
    val addedToInv: Int = 0,
    val droppedToWorld: Int = 0,
    val dropLocation: Location? = null,
    val droppedItems: Set<Item> = emptySet(),
)
