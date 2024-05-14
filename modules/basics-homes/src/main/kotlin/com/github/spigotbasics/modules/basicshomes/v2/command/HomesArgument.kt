package com.github.spigotbasics.modules.basicshomes.v2.command

import com.github.spigotbasics.core.command.parsed.arguments.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicshomes.data.Home
import com.github.spigotbasics.modules.basicshomes.v2.HomeStore
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomesArgument(name: String, private val homeStore: HomeStore) : CommandArgument<Home>(name) {
    override fun parse(
        sender: CommandSender,
        value: String,
    ): Home? {
        assert(sender is Player)
        val homeList = homeStore.getHomeList((sender as Player).uniqueId)
        return homeList.getHome(value)
    }

    override fun tabComplete(
        sender: CommandSender,
        typing: String,
    ): List<String> {
        assert(sender is Player)
        val homeList = homeStore.getHomeList((sender as Player).uniqueId)
        return homeList.toList().map { it.name }.toList().partialMatches(typing)
    }
}
