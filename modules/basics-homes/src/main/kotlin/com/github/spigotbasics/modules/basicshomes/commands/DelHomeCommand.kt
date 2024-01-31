package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

class DelHomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {

    override fun execute(context: BasicsCommandContext): Boolean {
        val result = module.parseHomeCmd(context)
        if(result is Either.Right) {
            return result.value
        }

        val home = (result as Either.Left).value
        val player = requirePlayerOrMustSpecifyPlayerFromConsole(context.sender)

        module.getHomeList(player.uniqueId).removeHome(home)
        module.msgHomeDeleted.tagUnparsed("home", home.name).sendToSender(player)
        return true
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        val sender = context.sender
        if (sender is Player) {
            if (context.args.size == 1) {
                return module.getHomeList(sender.uniqueId).listHomes().partialMatches(context.args[0])
            }
        }
        return mutableListOf()
    }
}