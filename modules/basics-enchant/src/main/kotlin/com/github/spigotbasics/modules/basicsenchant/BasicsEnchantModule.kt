package com.github.spigotbasics.modules.basicsenchant

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class BasicsEnchantModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val permission = permissionManager.createSimplePermission(
        "basics.enchant",
        "Allows the player to enchant items"
    )

    val msg = getConfig(ConfigName.MESSAGES)

    val enchantments = Bukkit.getRegistry(Enchantment::class.java)?.map { it.key.key }?.toList() ?: emptyList()

    fun msgEnchantedSelf(item: ItemStack, enchantment: Enchantment, level: Int)
    = msg.getMessage("enchanted-self")
        .tagParsed("item", item.type.name)
        .tagParsed("enchantment", enchantment.key.key)
        .tagParsed("level", level.toString())

    override fun onEnable() {
        createCommand("enchant", permission)
            .description("Enchants the item in the player's hand")
            .usage("<enchantment> [level]")
            .executor(EnchantExecutor())
            .register()
    }

    inner class EnchantExecutor : BasicsCommandExecutor(this@BasicsEnchantModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            val player = requirePlayerOrMustSpecifyPlayerFromConsole(context.sender) // TODO: Create a requirePlayer(Sender) method
            if(args.size == 0) return CommandResult.USAGE
            val item = requireItemInHand(player)
            val args = context.args
            val enchantment = getEnchantment(args[0])
            if(enchantment == null) failInvalidArgument(args[0])
            var level = (item.itemMeta?.getEnchantLevel(enchantment!!) ?: 0) + 1
            if(args.size > 1) {
                level = args[1].toIntOrNull() ?: throw failInvalidArgument(args[1]).asException()
            }
            if(level == 0) {
                item.removeEnchantment(enchantment!!) // TODO: message "removed enchantment"
                return CommandResult.SUCCESS
            }

            item.addUnsafeEnchantment(enchantment!!, level) // TODO: Separate permission for unsafe enchants, separate permission for max-level and for enchantment type
            msgEnchantedSelf(item, enchantment, level).sendToPlayer(player)
            return CommandResult.SUCCESS
        }

        override fun tabComplete(context: BasicsCommandContext): MutableList<kotlin.String>? {
            val args = context.args
            if(args.size == 1) {
                return enchantments.partialMatches(args[0])
            }
            return null
        }

        private fun getEnchantment(name: String): Enchantment? {
            return Bukkit.getRegistry(Enchantment::class.java)?.match(name.lowercase())
        }
    }

    override fun reloadConfig() {
        super.reloadConfig()
        msg.reload()
    }

}