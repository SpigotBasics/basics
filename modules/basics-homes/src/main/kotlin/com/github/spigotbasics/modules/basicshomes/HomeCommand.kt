package com.github.spigotbasics.modules.basicshomes

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.entity.Player

class HomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        if(context.sender !is Player) {
            module.plugin.messages.commandNotFromConsole.sendToSender(context.sender)
            return true
        }
        val player = context.sender as Player
        if(context.args.size != 1) {
            player.sendMessage("Usage: /home <name>") // Todo: Better message
            return true
        }
        val homeName = context.args[0]
        val homeList = module.homes[player.uniqueId] ?: error("Home list is null")
        val home = homeList.getHome(homeName)
        if(home == null) {
            player.sendMessage("Home not found") // TODO: Better message
            return true
        }
        player.teleport(home.toLocation())
        return true
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        if(context.sender !is Player) {
            return mutableListOf()
        }
        val player = context.sender as Player
        val homeList = module.homes[player.uniqueId] ?: error("Home list is null")
        return homeList.homes.keys.toMutableList().partialMatches(context.args[0])
    }


}