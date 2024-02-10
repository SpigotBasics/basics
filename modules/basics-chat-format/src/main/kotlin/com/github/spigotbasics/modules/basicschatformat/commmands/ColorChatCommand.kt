package com.github.spigotbasics.modules.basicschatformat.commmands

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import net.md_5.bungee.api.ChatColor
import org.bukkit.entity.Player
import java.util.Arrays
import java.util.stream.Collectors

class ColorChatCommand(private val module: BasicsChatFormatModule) : BasicsCommandExecutor(module) {
    override fun execute(context: BasicsCommandContext): CommandResult {
        if (context.sender !is Player) {
            module.plugin.messages.commandNotFromConsole.sendToSender(context.sender)
            return CommandResult.SUCCESS
        }
        val player = context.sender as Player

        var color = module.messageColor
        if (context.args.size == 1) {
            color = context.args[0]
        } else if (context.args.size > 1) {
            return CommandResult.USAGE
        }

        if (color != module.messageColor && !module.regex.matches(color)) {
            module.messages.colorInvalid(color).sendToSender(player)
            return CommandResult.SUCCESS
        }

        val chatDatum = module.getChatData(player.uniqueId)
        chatDatum.color = color
        module.messages.colorSet(color).sendToSender(player)

        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String> {
        return Arrays.stream(ChatColor.values()).map { it.name }.collect(Collectors.toList())
    }
}
