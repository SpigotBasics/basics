package com.github.spigotbasics.modules.basicschatformat.data.packages

import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import org.bukkit.permissions.Permissible

internal object GradientHexPackage : ChatColorPackage {
    override fun check(color: String): Boolean {
        val split = color.split(":")
        if (split.size < 2 || split.size > 3) {
            return false
        }

        for (s in split) {
            if (!HexColorPackage.check(s)) return false
        }
        return true
    }

    override fun setup(color: String): String {
        val builder = StringBuilder().append("gradient:")
        val split = color.split(":")
        for (s in split) {
            builder.append(s).append(":")
        }

        if (builder[builder.length - 1] == ':') {
            builder.deleteAt(builder.length - 1)
        }
        return builder.toString()
    }

    override fun hasPermission(
        permissible: Permissible,
        color: String,
    ): Boolean {
        if (permissible.hasPermission(BasicsChatFormatModule.instance.permissionGradientHex)) {
            return true
        }

        val split = color.split(":")
        for (s in split) {
            if (!HexColorPackage.hasPermission(permissible, s)) {
                return false
            }
        }
        return true
    }
}
