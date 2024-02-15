package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.inventory.ItemStack

class SnbtArg(name: String) : CommandArgument<ItemStack>(name) {
    override val greedy = true

    companion object {
        private val itemFactory = Bukkit.getItemFactory()
        private val logger = BasicsLoggerFactory.getCoreLogger(SnbtArg::class)
    }

    override fun parse(
        sender: CommandSender,
        value: String,
    ): ItemStack? {
        if (value.contains('{') && value.contains('}')) {
            return try {
                logger.debug(700, "Parsing SNBT: $value")
                val result = itemFactory.createItemStack(value)
                logger.debug(700, "Result: $result")
                result
            } catch (e: Exception) {
                logger.debug(700, "Failed to parse SNBT: ${e.message}")
                null
            }
        }
        return null
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        return emptyList()
    }
}
