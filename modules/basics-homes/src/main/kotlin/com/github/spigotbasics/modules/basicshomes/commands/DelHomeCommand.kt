package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.command.common.BasicsCommandExecutor
import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import org.bukkit.entity.Player

class DelHomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    private val messages = module.messages

    override fun execute(context: RawCommandContext): CommandResult {
        val result = module.parseHomeCmd(context)
        if (result is Either.Right) {
            return if (result.value) CommandResult.SUCCESS else CommandResult.USAGE
        }

        val home = (result as Either.Left).value
        val player = requirePlayerOrMustSpecifyPlayerFromConsole(context.sender)

        module.getHomeList(player.uniqueId).removeHome(home)
        messages.homeDeleted(home).sendToSender(player)
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: RawCommandContext): List<String> {
        val sender = context.sender
        if (sender is Player) {
            if (context.args.size == 1) {
                return module.getHomeList(sender.uniqueId).listHomeNames().partialMatches(context.args[0])
            }
        }
        return mutableListOf()
    }
}
