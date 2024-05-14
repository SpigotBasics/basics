package com.github.spigotbasics.modules.basicshomes.v2.command

import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.exceptions.WorldNotLoadedException
import com.github.spigotbasics.modules.basicshomes.data.Home
import com.github.spigotbasics.modules.basicshomes.v2.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.v2.HomeStore
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandHome(private val module: BasicsHomesModule, private val homeStore: HomeStore) :
    CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val homeList = homeStore.getHomeList(player.uniqueId)
        if (homeList.isEmpty()) {
            module.messages.homeNoneSet.concerns(player).sendToSender(player)
            return
        }

        val home = if (context["home"] != null) context["home"] as Home else homeList.getHome("home")

        if (home == null) {
            module.messages.homeNotFound.concerns(player).sendToSender(player)
            return
        }

        val location = home.location
        try {
            Spiper.teleportAsync(player, location.toLocation())
            module.messages.homeTeleport(home).concerns(player).sendToPlayer(player)
        } catch (exception: WorldNotLoadedException) {
            module.coreMessages.worldNotLoaded(location.world).concerns(player).sendToPlayer(player)
        }
    }
}
