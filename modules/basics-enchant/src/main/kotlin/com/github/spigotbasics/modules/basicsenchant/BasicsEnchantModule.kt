package com.github.spigotbasics.modules.basicsenchant

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.extensions.toHumanReadable
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class BasicsEnchantModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val permission =
        permissionManager.createSimplePermission(
            "basics.enchant",
            "Allows the player to enchant items",
        )

    val permissionUnsafeLevels =
        permissionManager.createSimplePermission(
            "basics.enchant.allowunsafe",
            "Allows the player to enchant items with unsafe levels",
        )

    val msg = getConfig(ConfigName.MESSAGES)

    val enchantments = Bukkit.getRegistry(Enchantment::class.java)?.map { it.key.key.lowercase() }?.toList() ?: emptyList()

    val enchantmentPermissions = Bukkit.getRegistry(Enchantment::class.java)?.associateWith { enchantment ->
        val name = enchantment.key.key.lowercase()
        permissionManager.createSimplePermission(
            "basics.enchant.${name}",
            "Allows the player to enchant items with the ${name.toHumanReadable()} enchantment",
        )
    } ?: emptyMap()

    fun msgEnchantedSelf(tag: EnchantOperationMessageTag) = msg.getMessage("enchanted-self").tags(tag)

    fun msgEnchantedOthers(
        tag: EnchantOperationMessageTag,
        player: Player,
    ) = msg.getMessage("enchanted-others")
        .tags(tag)
        .concerns(player)

    fun msgRemovedSelf(tag: EnchantOperationMessageTag) = msg.getMessage("removed-self").tags(tag)

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
            val args = context.args
            if (args.size == 0) return CommandResult.USAGE
            val item = requireItemInHand(player)
            val enchantment = getEnchantment(args[0]) ?: throw failInvalidArgument(args[0]).asException()
            var desiredLevel = (item.itemMeta?.getEnchantLevel(enchantment) ?: 0) + 1
            val maxLevel = enchantment.maxLevel
            if (args.size > 1) {
                desiredLevel = args[1].toIntOrNull() ?: throw failInvalidArgument(args[1]).asException()
            }

            // Unsafe levels require extra perm
            if(desiredLevel > maxLevel) {
                requirePermission(context.sender, permissionUnsafeLevels)
            }

            // Enchantment-specific permissions
            enchantmentPermissions[enchantment]?.let { requirePermission(context.sender, it) }

            val tag = EnchantOperationMessageTag(item, enchantment, desiredLevel)

            if (desiredLevel == 0) {
                item.removeEnchantment(enchantment) // TODO: message "removed enchantment"
                msgRemovedSelf(tag).sendToPlayer(player)
                return CommandResult.SUCCESS
            }

            item.addUnsafeEnchantment(
                enchantment,
                desiredLevel,
            ) // TODO: Separate permission for unsafe enchants, separate permission for max-level and for enchantment type
            msgEnchantedSelf(tag).sendToPlayer(player)
            return CommandResult.SUCCESS
        }

        override fun tabComplete(context: BasicsCommandContext): MutableList<kotlin.String>? {
            val args = context.args
            if (args.size == 1) {
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
