package com.github.spigotbasics.modules.basicshomes.commands

import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.messages.tags.MESSAGE_SPECIFIC_TAG_PREFIX
import com.github.spigotbasics.modules.basicshomes.BasicsHomesModule
import com.github.spigotbasics.modules.basicshomes.data.Home

class HomeListCommand(private val module: BasicsHomesModule) : BasicsCommandExecutor(module) {
    private val messages = module.messages

    override fun execute(context: RawCommandContext): CommandResult {
        if (context.args.isNotEmpty()) return CommandResult.USAGE
        val player = notFromConsole(context.sender)
        val homeList = module.getHomeList(player.uniqueId).toList()
        if (homeList.isEmpty()) {
            messages.homeNoneSet.sendToSender(player)
        } else {
            allHomes2msg(homeList).sendToSender(player)
        }

        return CommandResult.SUCCESS
    }

    fun home2msg(home: Home) =
        messages.homeListEntry
            .tags(home)

    fun allHomes2msg(homes: List<Home>): Message {
        val messageList =
            messageFactory.createMessage(
                List(homes.size) { index -> "<${MESSAGE_SPECIFIC_TAG_PREFIX}home${index + 1}>" }.joinToString("<#separator>"),
            )

        homes.mapIndexed { index, home -> messageList.tagMessage("home${index + 1}", home2msg(home)) }
        messageList.tagMessage("separator", messages.homeListSeparator)

        val message = messages.homeList.tagMessage("homes", messageList)
        return message
    }
}
