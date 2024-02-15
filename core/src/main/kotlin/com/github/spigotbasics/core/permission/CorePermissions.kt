package com.github.spigotbasics.core.permission

class CorePermissions(private val permissionManager: BasicsPermissionManager) {
    val useSelectors =
        permissionManager.createSimplePermission(
            "basics.selectors",
            "Allows using selectors (@p, etc)",
        )
}
