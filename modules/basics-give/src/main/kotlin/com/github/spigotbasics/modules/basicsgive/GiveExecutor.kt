package com.github.spigotbasics.modules.basicsgive

import com.github.spigotbasics.core.command.parsed.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class GiveExecutor(private val module: BasicsGiveModule) : CommandExecutor<GiveContext> {
    override fun execute(
        sender: CommandSender,
        context: GiveContext,
    ) {
        val item = ItemStack(context.material, context.amount)
        context.receiver.inventory.addItem(item)
        // println("Giving ${context.amount} of ${context.material} to ${context.receiver}")
        module.msgGiveOthers(context.receiver, item).sendToSender(sender)
    }
}
