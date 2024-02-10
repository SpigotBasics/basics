package com.github.spigotbasics.modules.basicschatformat.commmands

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.extensions.addAnd
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import com.github.spigotbasics.modules.basicschatformat.data.ChatData
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import java.util.Arrays
import kotlin.streams.toList

class ColorChatCommand(private val module: BasicsChatFormatModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): CommandResult {
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

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        return if (context.args.size == 1) {
            Arrays.stream(ChatColor.values()).map { it.name.lowercase() }.toList().addAnd("reset")
                .partialMatches(context.args[0])
        } else {
            mutableListOf()
        }
    }
}
