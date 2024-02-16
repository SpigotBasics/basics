package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.core.model.XYZCoords
import org.bukkit.command.CommandSender

class XYZCoordsArg(name: String) : CommandArgument<XYZCoords>(name) {
    override fun parse(sender: CommandSender, value: String): XYZCoords? {
        val split = value.split(" ")
        if (split.size != 3) return null
        val x = split[0].toDoubleOrNull() ?: return null
        val y = split[1].toDoubleOrNull() ?: return null
        val z = split[2].toDoubleOrNull() ?: return null
        return XYZCoords(x, y, z)
    }

    override val length = 3
}