package com.github.spigotbasics.core.extensions

import org.bukkit.permissions.Permissible

enum class Strategy {
    HIGHEST,
    LOWEST
}

val numberRegex = "\\d+".toRegex()

/**
 * Gets a permission value
 *
 * @param basePermission The permission to get the value of, for example `basics.sethome.multiple` for `basics.sethome.multiple.10`
 * @param strategy The strategy to use when getting the value
 */
fun Permissible.getPermissionNumberValue(basePermission: String, strategy: Strategy = Strategy.HIGHEST): Int? {

    val basePermissionWithDot = if (basePermission.endsWith('.')) basePermission else "$basePermission."
    val prefixLength = basePermissionWithDot.length

    val values = effectivePermissions
        .asSequence()
        .filter { it.value } // Only permission that are true
        .map { it.permission } // Get the permission string
        .filter { it.startsWith(basePermissionWithDot) }
        .map { it.substring(prefixLength) }
        .filter { it.matches(numberRegex) }
        .map { it.toInt() }

    return when (strategy) {
        Strategy.HIGHEST -> values.maxOrNull()
        Strategy.LOWEST -> values.minOrNull()
    }

}