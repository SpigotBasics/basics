package com.github.spigotbasics.core.messages.tags.providers

import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import org.bukkit.inventory.ItemStack

class ItemStackTag(private val item: ItemStack) : MessageTagProvider {
    override fun getMessageTags(): List<CustomTag> {
        val alwaysAvailable =
            listOf(
                CustomTag.parsed("item-type", item.type.name.toHumanReadable()),
                CustomTag.parsed("item-amount", item.amount.toString()),
                CustomTag.parsed("item-max-stack-size", item.maxStackSize.toString()),
            )
        val onlyWithMeta = mutableListOf<CustomTag>()

        // Unparsed tags, because they're player-changeable (ok lore maybe not - but still!)
        if (item.hasItemMeta()) {
            val meta = item.itemMeta!!
            onlyWithMeta.add(CustomTag.unparsed("item-display-name", meta.displayName))
            onlyWithMeta.add(CustomTag.unparsed("item-lore", meta.lore?.joinToString("\n") ?: ""))
        }
        return alwaysAvailable + onlyWithMeta
    }
}
