package com.github.spigotbasics.core.messages.tags.providers

import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.CustomTagType
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import org.bukkit.inventory.ItemStack

class ItemStackTag(private val item: ItemStack) : MessageTagProvider {
    override fun getMessageTags(): List<CustomTag> {
        val alwaysAvailable =
            listOf(
                CustomTag("item-type", item.type.name.toHumanReadable(), CustomTagType.PARSED),
                CustomTag("item-amount", item.amount.toString(), CustomTagType.PARSED),
                CustomTag("item-max-stack-size", item.maxStackSize.toString(), CustomTagType.PARSED),
            )
        val onlyWithMeta = mutableListOf<CustomTag>()

        // Unparsed tags, because they're player-changeable (ok lore maybe not - but still!)
        if (item.hasItemMeta()) {
            val meta = item.itemMeta!!
            onlyWithMeta.add(CustomTag("item-display-name", meta.displayName, CustomTagType.UNPARSED))
            onlyWithMeta.add(CustomTag("item-lore", meta.lore?.joinToString("\n") ?: "", CustomTagType.UNPARSED))
        }
        return alwaysAvailable + onlyWithMeta
    }
}
