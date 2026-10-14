package com.github.spigotbasics.modules.basicsmore

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.tags.providers.ItemStackTag
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class BasicsMoreModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val permission =
        permissionManager.createSimplePermission(
            "basics.more",
            "Allows the player to fill the stack in their hand",
        )

    private fun msgMore(item: ItemStack) = messages.getMessage("more").tags(ItemStackTag(item))

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("more", permission).mapContext {
            description("Fills the stack in your hand")

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
                    val item = player.inventory.itemInMainHand
                    if (item.type.isAir) {
                        coreMessages.notHavingItemInHand.sendToPlayer(player)
                        return
                    }

                    item.amount = maxOf(item.amount, item.maxStackSize)
                    msgMore(item).sendToPlayer(player)
                }
            },
        ).register()
    }
}
