package com.github.spigotbasics.modules.basicsworkbench

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BasicsWorkbenchModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val facade = plugin.facade.openInventoryFacade

    override fun onEnable() {
        makeParsedCommand("craftingtable", aliases = listOf("workbench", "craft")) {
            it.openWorkbench(null, true)
        }
        makeParsedCommand("cartographytable", aliases = listOf("cartography")) { facade.openCartographyTable(it) }
        makeParsedCommand("loom") { facade.openLoom(it) }
        makeParsedCommand("grindstone") { facade.openGrindstone(it) }
        makeParsedCommand("smithingtable", aliases = listOf("smithing")) { facade.openSmithingTable(it) }
        makeParsedCommand("stonecutter") { facade.openStonecutter(it) }
        makeParsedCommand("anvil", "Allows the player to open an anvil using /anvil", "Opens an anvil") { facade.openAnvil(it) }
    }

    private fun makeParsedCommand(
        name: String,
        permDescription: String = "Allows the player to open a $name using /$name",
        commandDescription: String = "Opens a $name",
        aliases: List<String> = mutableListOf(),
        openFunction: (Player) -> Unit,
    ) {
        commandFactory.parsedCommandBuilder(
            name,
            permissionManager.createSimplePermission("basics.$name", permDescription),
        ).mapContext {
            description(commandDescription)
            aliases(aliases)
            path {
                playerOnly()
            }
        }.executor(
            object : CommandContextExecutor<MapContext> {
                override fun execute(
                    sender: CommandSender,
                    context: MapContext,
                ) {
                    openFunction.invoke(sender as Player)
                }
            },
        ).register()
    }
}
