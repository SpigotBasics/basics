package com.github.spigotbasics.core.extensions

import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.model.DropResult
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.Item
import org.bukkit.inventory.BlockInventoryHolder
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

private val logger = BasicsLoggerFactory.getExtensionFileLogger("InventoryUtils")

/**
 * Attempts to add all items to the inventory and drops the remaining items to the world.
 *
 * @param inventory Inventory to add the items to
 * @param location Location where the items should be dropped. Default is the location, or if there is none, the location of the holder
 * @param naturally Whether the items should be dropped naturally or not
 * @param consumer Consumer for the spawned items
 * @receiver Inventory to add the items to
 * @return The result of the operation
 */
fun ItemStack.addOrDrop(
    inventory: Inventory,
    dropOverflow: Boolean = true,
    location: Location = inventory.findLocation(),
    naturally: Boolean = true,
    consumer: (Item) -> Unit = {},
): DropResult = inventory.addOrDrop(
    itemStack = this,
    dropOverflow = dropOverflow,
    location = location,
    naturally = naturally,
    consumer = consumer,
)

/**
 * Attempts to add all items to the inventory and drops the remaining items to the world.
 *
 * @param itemStack Items to add to the inventory
 * @param location Location where the items should be dropped. Default is the location, or if there is none, the location of the holder
 * @param naturally Whether the items should be dropped naturally or not
 * @param consumer Consumer for the spawned items
 * @receiver Inventory to add the items to
 * @return The result of the operation
 */
fun Inventory.addOrDrop(
    itemStack: ItemStack,
    dropOverflow: Boolean = true,
    location: Location = this.findLocation(),
    naturally: Boolean = true,
    consumer: (Item) -> Unit = {},
): DropResult {

    logger.debug(50, "Inventory.addOrDrop: itemStack = $itemStack, dropOverflow = $dropOverflow, location = $location, naturally = $naturally, consumer = $consumer")

    val totalAmount = itemStack.amount

    val remaining = this.addItem(itemStack)
    logger.debug(50, "  remaining = $remaining")
    if (remaining.isEmpty()) {
        logger.debug(50, "  All items added to inventory")
        return DropResult(addedToInv = totalAmount)
    }

    // We know there is only one item in the map, because the map is mapping the vararg index of Inventory#add and we only
    // passed one ItemStack. However, the method splits up stacks exceeding the max stack size and I don't know how
    // affects the return value, hence we rather sum all entries up
    val amountNotAdded = remaining.values.sumOf { it.amount }
    val amountAdded = totalAmount - amountNotAdded

    logger.debug(50, "  Not all items added to inventory: $amountAdded added, $amountNotAdded not added")

    if (!dropOverflow) {

        logger.debug(50, "  Not dropping overflow")

        return DropResult(
            addedToInv = amountAdded,
            droppedToWorld = 0,
        )
    }

    val world = location.world ?: throw WorldNotLoadedException(location)

    val droppedItems = mutableSetOf<Item>()

    for (item in remaining.values) {
        logger.debug(50, "  Dropping remaining item: $item")
        item?.let {
            val drop =
                world.dropItem(
                    location = location,
                    itemStack = it,
                    naturally = naturally,
                    consumer = consumer,
                )
            logger.debug(50, "  Dropped item: $drop")
            droppedItems.add(drop)
        }
    }

    return DropResult(
        addedToInv = amountAdded,
        droppedToWorld = amountNotAdded,
        dropLocation = location,
        droppedItems = droppedItems,
    )
}

fun World.dropItem(
    location: Location,
    itemStack: ItemStack,
    naturally: Boolean,
    consumer: ((Item) -> Unit)?,
): Item {
    return if (naturally) {
        logger.debug(20, "Dropping item naturally: $itemStack")
        this.dropItemNaturally(location, itemStack, consumer)
    } else {
        logger.debug(20, "Dropping item: $itemStack")
        this.dropItem(location, itemStack, consumer)
    }
}

fun Inventory.findLocation(): Location {
    val bukkitLocation = this.location
    if (bukkitLocation != null) return bukkitLocation

    val holder = this.holder
    if (holder == null) error("Inventory has no location and no holder")

    if (holder is Entity) return holder.location
    if (holder is BlockInventoryHolder) return holder.block.location

    error("Inventory holder is neither an entity nor a block")
}
