package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import com.github.spigotbasics.modules.basicschatformat.data.ChatFormatData
import com.github.spigotbasics.modules.basicschatformat.data.packages.ChatColorPackage
import com.github.spigotbasics.modules.basicschatformat.data.packages.ColorType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChatColorCommand(private val module: BasicsChatFormatModule) : CommandContextExecutor<MapContext> {
    override fun execute(
        sender: CommandSender,
        context: MapContext,
    ) {
        val player = sender as Player

        val rawColor = context["color"] as String?
        if (rawColor == null) {
            module.forgetPlayerData(player.uniqueId)
            module.messages.colorReset.concerns(player).sendToSender(player)
            return
        }
        val color = clean(rawColor)
        if (color == null) {
            module.messages.colorInvalid(rawColor).concerns(player).sendToSender(player)
            return
        }

        var colorPackage: ChatColorPackage? = null
        for (value in ColorType.entries) {
            if (value.colorPackage.check(color)) {
                colorPackage = value.colorPackage
                break
            }
        }

        if (colorPackage == null) {
            module.messages.colorInvalid(color).sendToSender(player)
            return
        }

        if (!colorPackage.hasPermission(player, color)) {
            module.coreMessages.noPermission.concerns(player).sendToSender(player)
            return
        }

        val setupColor = colorPackage.setup(color)
        module.chatFormatStore.setChatData(player.uniqueId, ChatFormatData(setupColor))
        module.messages.colorSet("<$setupColor>", rawColor).concerns(player).sendToSender(player)
    }

    private fun clean(dirtyColor: String): String? {
        val builder = StringBuilder()
        for ((index, c) in dirtyColor.withIndex()) {
            if (c == '<' && index != 0 || c == '>' && index != dirtyColor.length - 1) {
                return null
            }

            if (c == '<' || c == '>') {
                continue
            }

            builder.append(c)
        }

        return builder.toString()
    }
}
