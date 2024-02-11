package com.github.spigotbasics.core.command.parsed

import org.bukkit.inventory.ItemStack

class GiveCommandExecutor : CommandExecutor<GiveCommandContext> {
    override fun execute(context: GiveCommandContext) {
        context.receiver.inventory.addItem(ItemStack(context.material, context.amount))
        println("Giving ${context.amount} of ${context.material} to ${context.receiver}")
    }
}
