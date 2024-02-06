package com.github.spigotbasics.modules.basicsbroadcast

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class BroadcastExecutor(private val module: BasicsBroadcastModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): CommandResult {

        context.readFlags()

        val parseMini = context.popFlag("--parsed")
        failIfFlagsLeft(context)

        val args = context.args

        val text = args.joinToString(" ")
        var message = messageFactory.createPlainMessage(text)

        if(parseMini) {
            requirePermission(context.sender, module.parsedPerm)
            message = messageFactory.createMessage(text).concerns(context.sender as? Player)
        }

        message.sendToAllPlayers()
        message.sendToConsole()
        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        val options = mutableListOf<String>()
        if(context.sender.hasPermission(module.parsedPerm)) {
            options += "--parsed"
        }

        return if (context.args.size == 1) {
            StringUtil.copyPartialMatches(
                context.args[0],
                options,
                mutableListOf())
        } else {
            mutableListOf()
        }
    }
}