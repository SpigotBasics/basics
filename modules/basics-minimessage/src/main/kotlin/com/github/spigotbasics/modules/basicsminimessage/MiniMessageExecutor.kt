package com.github.spigotbasics.modules.basicsminimessage

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.module.BasicsModule
import org.bukkit.entity.Player
import org.bukkit.util.StringUtil

class MiniMessageExecutor(module: BasicsModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): Boolean {

        var parseMini = false
        val args = context.args.toMutableList()

        if (args[0] == "--parse") {
            requirePermission(context.sender, "basics.broadcast.parse")
            parseMini = true
            args.removeAt(0)
        }

        val text = args.joinToString(" ")
        val message = if (parseMini)
            messageFactory.createMessage(text)
        else
            messageFactory.createPlainMessage(text)

        message.sendToAllPlayers()
        return true
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        return if (context.args.size == 1) {
            StringUtil.copyPartialMatches(
                context.args[0],
                listOf("--parse"),
                mutableListOf())
        } else {
            mutableListOf()
        }
    }
}