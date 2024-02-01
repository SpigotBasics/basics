package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

class HomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        val result = module.parseHomeCmd(context)
        if(result is Either.Right) {
            return result.value
        }

        val home = (result as Either.Left).value
        val player = requirePlayerOrMustSpecifyPlayerFromConsole(context.sender)

        Spiper.teleportAsync(player, home.toLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN)
        module.msgHomeTeleported.tagUnparsed("home", home.name).sendToSender(player)
        return true
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        if(context.sender !is Player) {
            return mutableListOf()
        }
        val player = context.sender as Player
        val homeList = module.getHomeList(player.uniqueId)
        return homeList.listHomes().partialMatches(context.args[0])
    }


}