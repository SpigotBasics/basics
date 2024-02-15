package com.github.spigotbasics.modules.basicschatformat.commmands

import com.github.spigotbasics.core.command.common.BasicsCommandExecutor
import com.github.spigotbasics.core.command.common.CommandResult
import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.extensions.addAnd
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import com.github.spigotbasics.modules.basicschatformat.data.ChatData
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class ColorChatCommand(private val module: BasicsChatFormatModule) : BasicsCommandExecutor(module) {
    override fun execute(context: RawCommandContext): CommandResult {
        if (context.sender !is Player) {
            module.plugin.messages.commandNotFromConsole.sendToSender(context.sender)
            return CommandResult.SUCCESS
        }
        val player = context.sender as Player

        val color: String
        if (context.args.size == 1) {
            if (context.args[0].equals("reset", ignoreCase = true)) {
                module.resetChatColor(player.uniqueId)
                module.messages.colorReset.sendToSender(player)
                return CommandResult.SUCCESS
            }

            color = context.args[0]
        } else {
            return CommandResult.USAGE
        }

        if (color != module.messageColor && !module.regex.matches(color)) {
            module.messages.colorInvalid(color).sendToSender(player)
            return CommandResult.SUCCESS
        }

        val chatDatum = ChatData(color)
        module.setChatData(player.uniqueId, chatDatum)
        module.messages.colorSet(color).sendToSender(player)

        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: RawCommandContext): List<String> {
        return if (context.args.size == 1) {
            return ChatColor.entries.map { it.name.lowercase() }.addAnd("reset").partialMatches(context.args[0])
        } else {
            mutableListOf()
        }
    }
}
