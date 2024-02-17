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
        makeParsedCommand("craftingtable", listOf("workbench", "craft")) {
            it.openWorkbench(
                null,
                true,
            )
        }
        makeParsedCommand("cartographytable", listOf("cartography")) { facade.openCartographyTable(it) }
        makeParsedCommand("loom") { facade.openLoom(it) }
        makeParsedCommand("grindstone") { facade.openGrindstone(it) }
        makeParsedCommand("smithingtable", listOf("smithing")) { facade.openSmithingTable(it) }
        makeParsedCommand("stonecutter") { facade.openStonecutter(it) }
        makeParsedCommand("anvil") { facade.openAnvil(it) }
    }

    fun makeParsedCommand(
        name: String,
        openFunction: (Player) -> Unit,
    ) {
        makeParsedCommand(name, mutableListOf(), openFunction)
    }

    fun makeParsedCommand(
        name: String,
        aliases: List<String>,
        openFunction: (Player) -> Unit,
    ) {
        commandFactory.parsedCommandBuilder(
            name,
            permissionManager.createSimplePermission("basics.$name", "Allows a player to open a $name using /$name"),
        ).mapContext {
            description("Opens a $name workbench")
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
