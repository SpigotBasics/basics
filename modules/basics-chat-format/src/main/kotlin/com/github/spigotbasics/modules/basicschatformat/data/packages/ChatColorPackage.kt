package com.github.spigotbasics.modules.basicschatformat.data.packages

import org.bukkit.permissions.Permissible

/**
 * A Package of ChatColors that checks some format for the validity of a color
 */
interface ChatColorPackage {
    /**
     * Checks a given color string for its validity
     * @param color the color to check
     * @return true if the color is valid, otherwise false
     */
    fun check(color: String): Boolean

    /**
     * Sets up a color which has already been checked with [check]
     *
     * @param color the color
     * @return the setup color for insertion
     */
    fun setup(color: String): String

    /**
     * Checks if a permissible has permission for a given color
     * @param color the color to check
     * @return true if the permissible to check is valid for this color
     */
    fun hasPermission(
        permissible: Permissible,
        color: String,
    ): Boolean
}
