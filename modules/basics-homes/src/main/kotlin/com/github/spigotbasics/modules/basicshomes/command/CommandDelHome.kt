package com.github.spigotbasics.modules.basicshomes.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.HomeStore
import com.github.spigotbasics.modules.basicshomes.data.Home
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandDelHome(private val module: BasicsHomesModule, private val homeStore: HomeStore) :
    CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val homeList = homeStore.getHomeList(player.uniqueId)
        val home = if (context["home"] == null) homeList.getHome("home") else context["home"] as Home

        if (home == null) {
            module.messages.homeNotFound.concerns(player).sendToSender(player)
            return
        }

        homeList.removeHome(home)
        module.messages.homeRemoved(home).concerns(player).sendToSender(player)
    }
}
