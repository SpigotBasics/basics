package com.github.spigotbasics.modules.basicsenchant

import com.github.spigotbasics.core.command.parsed.arguments.BukkitRegistryArg
import com.github.spigotbasics.core.command.parsed.arguments.IntRangeArg
import com.github.spigotbasics.core.command.parsed.arguments.SelectorMultiEntityArg
import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.modules.basicsenchant.command.EnchantmentCommandOther
import com.github.spigotbasics.modules.basicsenchant.command.EnchantmentCommandSelf
import org.bukkit.enchantments.Enchantment

class BasicsEnchantModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val commandPermission =
        permissionManager.createSimplePermission("basics.enchant", "Allows players access to the /enchant command")
    private val commandOtherPermission = permissionManager.createSimplePermission(
        "basics.enchant.other",
        "Allows players access to the /enchant command to enchant others"
    )
    val unsafeLevelsPermission =
        permissionManager.createSimplePermission(
            "basics.enchant.unsafe.level",
            "Allows assignment of unsafe levels for enchantments",
        )
    val unsafeEnchantPermission =
        permissionManager.createSimplePermission(
            "basics.enchant.unsafe.enchant",
            "Allows assignment of unsafe enchantments to item",
        )

    override val messages: Messages = getConfig(ConfigName.MESSAGES, Messages::class.java)

    override fun onEnable() {
        val instance = this
        commandFactory.parsedCommandBuilder("enchant", commandPermission).mapContext {
            val basicExecutor = EnchantmentCommandSelf(instance)
            val otherExecutor = EnchantmentCommandOther(instance)
            usage = "<enchantment> [level]"
            path {
                playerOnly()
                arguments {
                    named("enchantment", BukkitRegistryArg("enchantment", Enchantment::class.java))
                }
                executor(basicExecutor)
            }

            path {
                playerOnly()
                arguments {
                    named("enchantment", BukkitRegistryArg("enchantment", Enchantment::class.java))
                    named("level", IntRangeArg("level", { 0 }, { 255 }))
                    executor(basicExecutor)
                }
            }

            path {
                permissions(commandOtherPermission)
                arguments {
                    named("targets", SelectorMultiEntityArg("targets"))
                    named("enchantment", BukkitRegistryArg("enchantment", Enchantment::class.java))
                    named("level", IntRangeArg("level", { 0 }, { 255 }))
                    executor(otherExecutor)
                }
            }

            path {
                permissions(commandOtherPermission)
                arguments {
                    named("player", SelectorSinglePlayerArg("player"))
                    named("enchantment", BukkitRegistryArg("enchantment", Enchantment::class.java))
                    named("level", IntRangeArg("level", { 0 }, { 255 }))
                    executor(otherExecutor)
                }
            }
        }.register()
    }
}
