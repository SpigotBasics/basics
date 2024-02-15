package com.github.spigotbasics.modules.basicscore.commands

import com.github.spigotbasics.core.Basics
import com.github.spigotbasics.core.command.parsed.CommandContextExecutor
import com.github.spigotbasics.core.command.parsed.context.MapContext
import org.bukkit.command.CommandSender

class ShowTpsCommand : CommandContextExecutor<MapContext> {
    override fun execute(sender: CommandSender, context: MapContext) {
        sender.sendMessage("TPS: " + Basics.nms.getTps().joinToString())
    }


}
