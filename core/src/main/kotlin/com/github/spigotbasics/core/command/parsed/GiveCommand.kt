package com.github.spigotbasics.core.command.parsed

import org.bukkit.inventory.ItemStack

class GiveCommand : ParsedCommandExecutor<GiveCommandContext>() {
    override fun execute(context: GiveCommandContext) {
        val itemStack = ItemStack(context.item)
        context.receiver.inventory.addItem(itemStack)
        context.sender.sendMessage("Gave ${context.receiver.name} ${itemStack.type}!")
    }
}
