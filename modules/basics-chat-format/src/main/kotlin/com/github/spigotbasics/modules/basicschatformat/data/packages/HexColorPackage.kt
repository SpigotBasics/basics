package com.github.spigotbasics.modules.basicschatformat.data.packages

import com.github.spigotbasics.modules.basicschatformat.BasicsChatFormatModule
import org.bukkit.permissions.Permissible

internal object HexColorPackage : ChatColorPackage {
    private val ZERO = Character.getNumericValue('0')
    private val NINE = Character.getNumericValue('9')
    private val UPPER_A = Character.getNumericValue('A')
    private val LOWER_A = Character.getNumericValue('a')
    private val UPPER_F = Character.getNumericValue('F')
    private val LOWER_F = Character.getNumericValue('f')

    override fun check(color: String): Boolean {
        for ((index, c) in color.withIndex()) {
            if (index == 0) continue
            val number = Character.getNumericValue(c)
            if (!(number in ZERO..NINE || number in UPPER_A..UPPER_F || number in LOWER_A..LOWER_F)) return false
        }
        return color.startsWith("#") && color.length <= 7
    }

    override fun setup(color: String): String {
        return "color:$color"
    }

    override fun hasPermission(
        permissible: Permissible,
        color: String,
    ): Boolean {
        return permissible.hasPermission(BasicsChatFormatModule.instance.permissionHexColors) ||
            permissible.hasPermission(
                "basics.chatcolor.${color.uppercase()}",
            )
    }
}
