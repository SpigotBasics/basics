package com.github.spigotbasics.modules.basicshomes.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.extensions.getPermissionNumberValue
import com.github.spigotbasics.core.model.toSimpleLocation
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.HomeStore
import com.github.spigotbasics.modules.basicshomes.data.Home
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandSetHome(private val module: BasicsHomesModule, private val homeStore: HomeStore) :
    CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player
        val name = if (context["name"] != null) context["name"] as String else "home"

        if (!name.matches(module.regex.toRegex())) {
            module.messages.homeRegexViolated(module.regex).concerns(player).sendToSender(player)
            return
        }

        val homeList = homeStore.getHomeList(player.uniqueId)

        val maxAllowed = player.getPermissionNumberValue(module.permissionSetHomeMultiple.name) ?: 2
        if (!player.hasPermission(module.permissionSetHomeUnlimited) && homeList.size == maxAllowed) {
            module.messages.homeLimitReached(maxAllowed).concerns(player).sendToSender(player)
            return
        }

        val home = Home(name, player.location.toSimpleLocation())
        homeList.addHome(home)
        module.messages.homeSet(home).sendToSender(sender)
    }
}
