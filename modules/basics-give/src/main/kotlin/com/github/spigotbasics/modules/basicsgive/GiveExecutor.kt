package com.github.spigotbasics.modules.basicsgive

import com.github.spigotbasics.core.command.parsed2.CommandExecutor
import com.github.spigotbasics.core.command.parsed2.GiveCommandContext
import org.bukkit.inventory.ItemStack

class GiveExecutor : CommandExecutor<GiveContext> {
    override fun execute(context: GiveContext) {
        context.receiver.inventory.addItem(ItemStack(context.material, context.amount))
        println("Giving ${context.amount} of ${context.material} to ${context.receiver}")
    }
}