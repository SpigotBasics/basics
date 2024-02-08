package com.github.spigotbasics.modules.basicsenchant

import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.extensions.toRoman
import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

data class EnchantOperationMessageTag(
    val item: ItemStack,
    val enchantment: Enchantment,
    val level: Int,
) : MessageTagProvider {
    override fun getMessageTags(): List<CustomTag> {
        return listOf(
            CustomTag.parsed("item", item.type.name.toHumanReadable()),
            CustomTag.parsed("enchantment", enchantment.key.key.toHumanReadable()),
            CustomTag.parsed("level", level.toString()),
            CustomTag.parsed("roman-level", level.toRoman()),
        )
    }
}
