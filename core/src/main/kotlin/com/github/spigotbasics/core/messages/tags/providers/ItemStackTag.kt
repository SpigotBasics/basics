package com.github.spigotbasics.core.messages.tags.providers

import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.extensions.toSnbtWithType
import com.github.spigotbasics.core.extensions.toSnbtWithoutType
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MESSAGE_SPECIFIC_TAG_PREFIX
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.api.BinaryTagHolder
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.inventory.ItemStack

class ItemStackTag(private val item: ItemStack) : MessageTagProvider {
    override fun getMessageTags(): List<CustomTag> {
        val alwaysAvailable =
            listOf(
                CustomTag.parsed("item-type", item.type.name.toHumanReadable()),
                CustomTag.parsed("item-type-human", item.type.name.toHumanReadable()),
                CustomTag.parsed("item-amount", item.amount.toString()),
                CustomTag.parsed("item-max-stack-size", item.maxStackSize.toString()),
                CustomTag.parsed("item-snbt", item.toSnbtWithType()),
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

    override fun getTagProviders(): List<Any> {
        return listOf(getHoverTag())
    }

    private fun getHoverTag(): TagResolver {
        return Placeholder.styling("${MESSAGE_SPECIFIC_TAG_PREFIX}item-hover", HoverEvent.showItem(Key.key("minecraft:" + item.type.name.lowercase()), item.amount, BinaryTagHolder.binaryTagHolder(item.toSnbtWithoutType())))
    }
}
