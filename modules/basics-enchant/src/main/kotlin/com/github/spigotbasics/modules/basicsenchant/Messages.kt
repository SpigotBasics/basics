package com.github.spigotbasics.modules.basicsenchant

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.extensions.toRoman
import com.github.spigotbasics.core.messages.Message
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Messages(context: ConfigInstantiationContext) : SavedConfig(context) {
    fun enchantmentSelf(
        item: ItemStack,
        enchantment: Enchantment,
        level: Int,
    ): Message = modifyEnchantMessage(getMessage("enchanted-self"), item, enchantment, level)

    fun enchantEntity(
        entity: LivingEntity,
        item: ItemStack,
        enchantment: Enchantment,
        level: Int,
    ): Message =
        modifyEnchantMessage(
            getMessage("enchanted-entity"),
            item,
            enchantment,
            level,
        ).tagParsed("entity", entity.type.key.key.toHumanReadable())

    fun enchantPlayer(
        player: Player,
        item: ItemStack,
        enchantment: Enchantment,
        level: Int,
    ): Message = modifyEnchantMessage(getMessage("enchanted-others"), item, enchantment, level).tagParsed("player", player.name)

    fun enchantInvalidCombinationSelf(
        item: ItemStack,
        enchantment: Enchantment,
    ): Message =
        getMessage("enchanted-invalid-combination-self")
            .tagParsed("item", item.type.name.toHumanReadable())
            .tagParsed("enchantment", enchantment.key.key.toHumanReadable())

    fun enchantInvalidCombinationEntity(
        entity: LivingEntity,
        item: ItemStack,
        enchantment: Enchantment,
    ): Message =
        getMessage("enchanted-invalid-combination-entity")
            .tagParsed("entity", entity.type.key.key.toHumanReadable())
            .tagParsed("item", item.type.name.toHumanReadable())
            .tagParsed("enchantment", enchantment.key.key.toHumanReadable())

    fun enchantInvalidCombinationOther(
        item: ItemStack,
        enchantment: Enchantment,
    ): Message =
        getMessage("enchanted-invalid-combination-others")
            .tagParsed("item", item.type.name.toHumanReadable())
            .tagParsed("enchantment", enchantment.key.key.toHumanReadable())

    private fun modifyEnchantMessage(
        message: Message,
        item: ItemStack,
        enchantment: Enchantment,
        level: Int,
    ): Message =
        message.tagParsed("item", item.type.name.toHumanReadable())
            .tagParsed("enchantment", enchantment.key.key.toHumanReadable())
            .tagParsed("level", level.toString())
            .tagUnparsed("roman-level", level.toRoman())
}
