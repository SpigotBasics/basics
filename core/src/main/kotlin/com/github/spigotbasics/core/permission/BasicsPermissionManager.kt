package com.github.spigotbasics.core.permission

import com.github.spigotbasics.core.logger.BasicsLogger
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault

class BasicsPermissionManager(val logger: BasicsLogger) {
    private val permissions = mutableListOf<Permission>()

    fun createSimplePermission(
        permission: String,
        description: String? = null,
        defaultValue: PermissionDefault = PermissionDefault.OP,
    ): Permission {
        if (permissions.any { it.name == permission }) {
            error("Permission $permission already registered in this module")
        }

        val perm =
            Permission(
                permission,
                description,
                defaultValue,
            )

        val existing = Bukkit.getPluginManager().getPermission(permission)

        if (existing != null) {
            if (existing.default != defaultValue) {
                logger.warning("Permission $permission already registered with different default value")
            }
            if (existing.description != description) {
                logger.warning("Permission $permission already registered with different description")
            }
            if (existing.children != perm.children) {
                logger.warning("Permission $permission already registered with different children")
            }
            permissions.add(existing)
            return existing
        }

        Bukkit.getPluginManager().addPermission(perm)
        permissions.add(perm)
        return perm
    }

    fun unregisterAll() {
        permissions.forEach {
            Bukkit.getPluginManager().removePermission(it)
        }
        permissions.clear()
    }
}
