package com.github.spigotbasics.core.permission

import com.github.spigotbasics.core.logger.BasicsLoggerFactory

object CorePermissions {
    private val pm = BasicsPermissionManager(BasicsLoggerFactory.getCoreLogger(CorePermissions::class))

    val useSelectors =
        pm.createSimplePermission(
            "basics.selectors",
            "Allows using selectors (@p, etc)",
        )
}
