package com.github.spigotbasics.modules.basicshomes

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import org.bukkit.entity.Player

class SetHomeCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        if(context.sender !is Player) {
            module.plugin.messages.commandNotFromConsole.sendToSender(context.sender)
            return true
        }
        val player = context.sender as Player
        val location = player.location
        val home = Home(location)
        val homeList = module.homes[player.uniqueId] ?: error("Home list is null")
        if(context.args.size != 1) {
            player.sendMessage("Usage: /sethome <name>") // Todo: Better message
            return true
        }
        val homeName = context.args[0]
        homeList.addHome(homeName, home)
        player.sendMessage("Home set")
        return true
    }
}