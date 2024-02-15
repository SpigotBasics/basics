package com.github.spigotbasics.core.command.parsed.arguments

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class SnbtArg(name: String) : CommandArgument<ItemStack>(name) {

    override val greedy = true

    companion object {
        private val itemFactory = Bukkit.getItemFactory()
    }

    override fun parse(sender: CommandSender, value: String): ItemStack? {
        if (value.contains('{') && value.contains('}')) {
            return try {
                itemFactory.createItemStack(value)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    override fun tabComplete(sender: CommandSender, typing: String): List<String> {
        return emptyList()
    }
}