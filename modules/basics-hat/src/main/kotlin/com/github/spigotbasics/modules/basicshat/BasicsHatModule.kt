package com.github.spigotbasics.modules.basicshat

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

class BasicsHatModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.hat",
            "Allows the player to wear the item in their hand as a hat",
        )

    private val msgEquipped
        get() = messages.getMessage("hat-equipped")
    private val msgRemoved
        get() = messages.getMessage("hat-removed")
    private val msgCursed
        get() = messages.getMessage("hat-cursed")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("hat", permission).mapContext {
            description("Wear the item in your hand as a hat")

            path {
                playerOnly()
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    val player = sender as Player
                    val inventory = player.inventory
                    val hand = inventory.itemInMainHand
                    val helmet = inventory.helmet

                    if (hand.type.isAir && (helmet == null || helmet.type.isAir)) {
                        coreMessages.notHavingItemInHand.sendToPlayer(player)
                        return
                    }
                    if (player.gameMode != GameMode.CREATIVE && helmet?.containsEnchantment(Enchantment.BINDING_CURSE) == true) {
                        msgCursed.sendToPlayer(player)
                        return
                    }

                    inventory.helmet = hand
                    inventory.setItemInMainHand(helmet)
                    (if (hand.type.isAir) msgRemoved else msgEquipped).sendToPlayer(player)
                }
            },
        ).register()
    }
}
