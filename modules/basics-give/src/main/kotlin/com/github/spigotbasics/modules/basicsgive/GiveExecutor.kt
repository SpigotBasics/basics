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

        context.receiver.inventory.addItem(item.clone())

        val msg =
            if (sender === context.receiver) {
                module.msgGiveSelf(context.receiver, item)
            } else {
                module.msgGiveOthers(context.receiver, item)
            }

        msg.sendToSender(sender)
    }
}
