package com.github.spigotbasics.core.permission

import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionDefault
import java.util.logging.Logger

class BasicsPermissionManager(val logger: Logger) {

    private val permissions = mutableListOf<Permission>()

    fun createSimplePermission(
        permission: String,
        description: String? = null,
        defaultValue: PermissionDefault = PermissionDefault.OP

    ): Permission {

        if(permissions.any { it.name == permission }) {
            logger.warning("Permission $permission already registered in this module")
        }

        val perm = Permission(
            permission,
            description,
            defaultValue
        )

        val existing = Bukkit.getPluginManager().getPermission(permission)
        if(existing != null) {
            if(existing.default != defaultValue) {
                logger.warning("Permission $permission already registered with different default value")
            }
            permissions.add(existing)
            return existing
        }

        try {
            Bukkit.getPluginManager().addPermission(perm)
        } catch (e: IllegalArgumentException) {
            logger.warning("Failed to register permission $permission: ${e.message}")
        }
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