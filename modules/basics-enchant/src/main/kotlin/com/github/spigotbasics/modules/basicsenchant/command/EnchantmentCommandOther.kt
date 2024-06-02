package com.github.spigotbasics.modules.basicsenchant.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.modules.basicsenchant.BasicsEnchantModule
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

class EnchantmentCommandOther(private val module: BasicsEnchantModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val targets = mutableListOf<LivingEntity>()
        if (context["targets"] is List<*>) {
            targets.addAll((context["targets"] as List<*>).filterIsInstance<LivingEntity>())
        } else if (context["player"] is Player) {
            targets.add(context["player"] as LivingEntity)
        }

        val enchantment = context["enchantment"] as Enchantment
        val level = context["level"] as Int

        if (level > enchantment.maxLevel && !sender.hasPermission(module.unsafeLevelsPermission)) {
            module.coreMessages.invalidValueForArgument("level", level.toString()).concerns(sender as? Player).sendToSender(sender)
            return
        }

        targets.forEach {
            val equipment = it.equipment ?: return
            val item = equipment.itemInMainHand
            if (item.type == Material.AIR) {
                return@forEach
            }

            if (!enchantment.canEnchantItem(item) && sender.hasPermission(module.unsafeEnchantPermission)) {
                val message: Message =
                    if (it is Player) {
                        module.messages.enchantInvalidCombinationOther(item, enchantment).concerns(it)
                    } else {
                        module.messages.enchantInvalidCombinationEntity(it, item, enchantment)
                    }
                message.sendToSender(sender)
                return
            }

            val meta = item.itemMeta!!
            meta.addEnchant(enchantment, level, sender.hasPermission(module.unsafeLevelsPermission))
            item.itemMeta = meta
            equipment.setItemInMainHand(item)

            val message =
                if (it is Player) {
                    module.messages.enchantPlayer(it, item, enchantment, level).concerns(it)
                } else {
                    module.messages.enchantEntity(it, item, enchantment, level)
                }

            message.sendToSender(sender)
        }
    }
}
