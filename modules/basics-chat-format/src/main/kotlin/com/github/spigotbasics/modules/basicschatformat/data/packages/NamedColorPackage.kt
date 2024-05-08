package com.github.spigotbasics.modules.basicschatformat.data.packages

import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import org.bukkit.permissions.Permissible

internal object NamedColorPackage : ChatColorPackage {
    private val NAMED_COLORS =
        setOf(
            "black",
            "dark_blue",
            "dark_green",
            "dark_aqua",
            "dark_red",
            "dark_purple",
            "gold",
            "gray",
            "dark_gray",
            "blue",
            "green",
            "aqua",
            "red",
            "light_purple",
            "yellow",
            "white",
        )

    override fun check(color: String): Boolean {
        return NAMED_COLORS.contains(color)
    }

    override fun setup(color: String): String {
        return "color:$color"
    }

    override fun hasPermission(
        permissible: Permissible,
        color: String,
    ): Boolean {
        return permissible.hasPermission(BasicsChatFormatModule.instance.permissionNamedColors) ||
            permissible.hasPermission(
                "basics.chatcolor.$color".lowercase(),
            )
    }
}
