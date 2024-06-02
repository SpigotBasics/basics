package com.github.spigotbasics.modules.basicshomes.command

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.tags.MESSAGE_SPECIFIC_TAG_PREFIX
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.HomeStore
import com.github.spigotbasics.modules.basicshomes.data.Home
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandHomeList(private val module: BasicsHomesModule, private val homeStore: HomeStore) :
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
        homeListToMessage(homeList.toList()).concerns(player).sendToSender(player)
    }

    private fun homeToMessage(home: Home) = module.messages.homeListEntry.tags(home)

    private fun homeListToMessage(homes: List<Home>): Message {
        val message =
            module.messageFactory.createMessage(
                List(homes.size) { i -> "<${MESSAGE_SPECIFIC_TAG_PREFIX}home${i + 1}>" }.joinToString("\n"),
            )

        homes.mapIndexed { i, home -> message.tagMessage("home${i + 1}", homeToMessage(home)) }
        message.tagMessage("separator", module.messages.homeListSeparator)
        return module.messages.homeList.tagMessage("homes", message)
    }
}
