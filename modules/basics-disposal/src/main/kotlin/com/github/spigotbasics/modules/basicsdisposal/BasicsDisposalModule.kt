package com.github.spigotbasics.modules.basicsdisposal

import com.github.spigotbasics.core.command.common.BasicsCommandContextHandler
import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.Bukkit

class BasicsDisposalModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context), BasicsCommandContextHandler {
    val permission = permissionManager.createSimplePermission("basics.disposal", "Allows to dispose items using /disposal")
    val title get() = messages.getMessage("inventory-title")

    override fun onEnable() {
        commandFactory.rawCommandBuilder("disposal", permission)
            .executor { content ->
                val player = requirePlayer(content.sender)
                val inv = Bukkit.createInventory(null, 6 * 9, title.toLegacyString())
                player.openInventory(inv)
                CommandResult.SUCCESS
            }.register()
    }
}
