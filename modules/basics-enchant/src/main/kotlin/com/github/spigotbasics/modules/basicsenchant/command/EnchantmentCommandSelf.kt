package com.github.spigotbasics.modules.basicsenchant.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.modules.basicsenchant.BasicsEnchantModule
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class EnchantmentCommandSelf(private val module: BasicsEnchantModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val enchantment = context["enchantment"] as Enchantment
        val level = context["level"] as Int? ?: 1

        if (level > enchantment.maxLevel && !player.hasPermission(module.unsafeLevelsPermission)) {
            module.coreMessages.invalidValueForArgument("level", level.toString()).concerns(player).sendToSender(sender)
            return
        }

        val itemInHand = player.inventory.itemInMainHand

        if (!enchantment.canEnchantItem(itemInHand) && player.hasPermission(module.unsafeEnchantPermission)) {
            module.messages.enchantInvalidCombinationSelf(itemInHand, enchantment).concerns(player).sendToPlayer(sender)
            return
        }

        val meta = itemInHand.itemMeta!!
        meta.addEnchant(enchantment, level, player.hasPermission("enchantment.level"))
        itemInHand.itemMeta = meta

        module.messages.enchantmentSelf(itemInHand, enchantment, level)
    }
}
