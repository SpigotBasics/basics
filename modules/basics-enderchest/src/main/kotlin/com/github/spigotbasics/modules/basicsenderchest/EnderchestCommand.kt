package com.github.spigotbasics.modules.basicsenderchest

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult

class EnderchestCommand(private val module: BasicsEnderchestModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): CommandResult? {
        val sender = requirePlayer(context.sender)
        var targetPlayer = sender
        val args = context.args
        if (args.size > 1) return CommandResult.USAGE
        if (args.size == 1) {
            requirePermission(context.sender, module.permissionOthers)
            targetPlayer = requirePlayer(context.args[0])
        }
        val view = sender.openInventory(targetPlayer.enderChest)
        view?.title = module.inventoryTitle.concerns(targetPlayer).toLegacyString()
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        if (context.sender.hasPermission(module.permissionOthers)) {
            return null
        }
        return mutableListOf()
    }
}
