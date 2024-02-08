package com.github.spigotbasics.modules.basicsenchant

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Bukkit
import org.bukkit.enchantments.Enchantment

class BasicsEnchantModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val permission = permissionManager.createSimplePermission(
        "basics.enchant",
        "Allows the player to enchant items"
    )

    val enchantments = Bukkit.getRegistry(Enchantment::class.java)?.map { it.key.key }?.toList() ?: emptyList()

    override fun onEnable() {
        createCommand("enchant", permission)
            .description("Enchants the item in the player's hand")
            .usage("<enchantment> [level]")
            .executor(EnchantExecutor())
            .register()
    }

    inner class EnchantExecutor : BasicsCommandExecutor(this@BasicsEnchantModule) {
        override fun execute(context: BasicsCommandContext): CommandResult? {
            return null
        }

        override fun tabComplete(context: BasicsCommandContext): MutableList<kotlin.String>? {
            val args = context.args
            if(args.size == 1) {
                return enchantments.partialMatches(args[0])
            }
            return null
        }
    }

}