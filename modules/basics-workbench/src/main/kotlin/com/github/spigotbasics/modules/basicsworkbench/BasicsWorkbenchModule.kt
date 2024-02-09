package com.github.spigotbasics.modules.basicsworkbench

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsWorkbenchModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val facade = plugin.facade.openInventoryFacade

    val permissionCraftingTable =
        permissionManager.createSimplePermission(
            "basics.craftingtable",
            "Allows the player to open a crafting table using /craftingtable",
        )

    val permissionCartographyTable =
        permissionManager.createSimplePermission(
            "basics.cartographytable",
            "Allows the player to open a cartography table using /cartographytable",
        )

    val permissionLoom =
        permissionManager.createSimplePermission(
            "basics.loom",
            "Allows the player to open a loom using /loom",
        )

    val permissionGrindstone =
        permissionManager.createSimplePermission(
            "basics.grindstone",
            "Allows the player to open a grindstone using /grindstone",
        )

    val permissionSmithingTable =
        permissionManager.createSimplePermission(
            "basics.smithingtable",
            "Allows the player to open a loom using /smithingtable",
        )

    val permissionStonecutter =
        permissionManager.createSimplePermission(
            "basics.stonecutter",
            "Allows the player to open a stone cutter using /stonecutter",
        )

    val permissionAnvil =
        permissionManager.createSimplePermission(
            "basics.anvil",
            "Allows the player to open an anvil using /anvil",
        )

    override fun onEnable() {
        createCommand("craftingtable", permissionCraftingTable)
            .description("Opens a crafting table")
            .aliases(listOf("workbench"))
            .executor(WorkbenchExecutor())
            .register()

        createCommand("cartographytable", permissionCartographyTable)
            .description("Opens a cartography table")
            .executor(CartographyExecutor())
            .register()

        createCommand("loom", permissionLoom)
            .description("Opens a loom")
            .executor(LoomExecutor())
            .register()

        createCommand("grindstone", permissionGrindstone)
            .description("Opens a grindstone")
            .executor(GrindstoneExecutor())
            .register()

        createCommand("smithingtable", permissionSmithingTable)
            .description("Opens a smithing table")
            .executor(SmithingTableExecutor())
            .register()

        createCommand("stonecutter", permissionStonecutter)
            .description("Opens a stonecutter")
            .executor(StonecutterExecutor())
            .register()

        createCommand("anvil", permissionAnvil)
            .description("Opens an anvil")
            .executor(AnvilExecutor())
            .register()
    }

    inner class WorkbenchExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            notFromConsole(context.sender).openWorkbench(null, true)
            return CommandResult.SUCCESS
        }
    }

    inner class CartographyExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            facade.openCartographyTable(notFromConsole(context.sender))
            return CommandResult.SUCCESS
        }
    }

    inner class LoomExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            facade.openLoom(notFromConsole(context.sender))
            return CommandResult.SUCCESS
        }
    }

    inner class GrindstoneExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            facade.openGrindstone(notFromConsole(context.sender))
            return CommandResult.SUCCESS
        }
    }

    inner class SmithingTableExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            facade.openSmithingTable(notFromConsole(context.sender))
            return CommandResult.SUCCESS
        }
    }

    inner class StonecutterExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            facade.openStonecutter(notFromConsole(context.sender))
            return CommandResult.SUCCESS
        }
    }

    inner class AnvilExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): CommandResult {
            facade.openAnvil(notFromConsole(context.sender))
            return CommandResult.SUCCESS
        }
    }
}
