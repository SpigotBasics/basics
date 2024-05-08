package com.github.spigotbasics.modules.basicsdisposal

import com.github.spigotbasics.core.command.common.BasicsCommandContextHandler
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsDisposalModule(context: ModuleInstantiationContext) :
    AbstractBasicsModule(context),
    BasicsCommandContextHandler {
    private val permission =
        permissionManager.createSimplePermission("basics.disposal", "Allows to dispose items using /disposal")
    val title get() = messages.getMessage("inventory-title")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("disposal", permission).mapContext {
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
                    val inv = Bukkit.createInventory(null, 6 * 9, title.toLegacyString())
                    player.openInventory(inv)
                }
            },
        ).register()
    }
}
