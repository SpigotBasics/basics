package com.github.spigotbasics.core.extensions

import org.bukkit.permissions.Permissible
import java.util.regex.Pattern

enum class Strategy() {
    HIGHEST,
    LOWEST
}

/**
 * Gets a permission value
 *
 * @param basePermission The permission to get the value of, for example `basics.homes` for `basics.homes.10`
 * @param strategy The strategy to use when getting the value
 */
fun Permissible.getPermissionNumberValue(basePermission: String, strategy: Strategy = Strategy.HIGHEST): Int? {
    val regex = "^${Pattern.quote(basePermission)}\\.{\\d}+$".toRegex()
    val values = effectivePermissions
        .filter { it.value }
        .map { it.permission }
        .filter { it.matches(regex) }
        .map { it.substringAfterLast(".") }
        .map { it.toInt() }

    return when(strategy) {
        Strategy.HIGHEST -> values.maxOrNull()
        Strategy.LOWEST -> values.minOrNull()
    }

}