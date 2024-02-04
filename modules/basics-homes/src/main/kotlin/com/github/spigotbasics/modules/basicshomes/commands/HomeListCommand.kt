package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.common.leftOrNull
import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.data.Home

class HomeListCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {
        val player = notFromConsole(context.sender)
        val homeList = module.getHomeList(player.uniqueId).listHomes()
        if (homeList.isEmpty()) {
            module.msgHomeNoneSet.sendToSender(player)
        } else {
            allHomes2msg(homeList).sendToSender(player)
        }

        return true
    }

    fun home2msg(home: Home) =
        module.msgHomeListEntry
            .tagParsed("name", home.name)
            .tagParsed("world", home.location.world)
            .tagParsed("x", home.location.x.toInt().toString())
            .tagParsed("y", home.location.y.toInt().toString())
            .tagParsed("z", home.location.z.toInt().toString())

    fun allHomes2msg(homes: List<Home>): Message {
        val messageList =
            messageFactory.createMessage(List(homes.size) { index -> "<home${index + 1}>" }.joinToString("<separator>"))

        homes.mapIndexed() { index, home -> messageList.tagMessage("home${index + 1}", home2msg(home)) }
        messageList.tagMessage("separator", module.msgHomeListSeparator)

        val message = module.msgHomeList.tagMessage("homes", messageList)
        return message
    }
}