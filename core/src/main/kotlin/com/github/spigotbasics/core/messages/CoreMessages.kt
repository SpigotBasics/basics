package com.github.spigotbasics.core.messages

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig
import org.bukkit.permissions.Permission

/**
 * Provides messages used by the core classes, or are commonly used in other modules
 */
class CoreMessages(context: ConfigInstantiationContext) : SavedConfig(context) {
    val noSafeLocationFound get() = getMessage("no-safe-location-found")
    val noPermission get() = getMessage("no-permission")
    val commandNotFromConsole get() = getMessage("command-not-from-console")
    val mustSpecifyPlayerFromConsole get() = getMessage("must-specify-player-from-console")
    val commandModuleDisabled get() = getMessage("command-module-disabled")
    val failedToLoadDataOnJoin get() = getMessage("failed-to-load-data-on-join")
    fun noPermission(permission: Permission) = getMessage("no-permission").tagParsed("permission", permission.name)
    fun unknownOption(option: String) = getMessage("unknown-option").tagUnparsed("option", option)
    fun invalidArgument(argument: String) = getMessage("invalid-argument").tagUnparsed("argument", argument)
    fun playerNotFound(name: String) = getMessage("player-not-found").tagUnparsed("argument", name)
    fun worldNotFound(name: String) = getMessage("world-not-found").tagUnparsed("argument", name)

}