package com.github.spigotbasics.modules.basicshomes

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.modules.basicshomes.data.Home

class Messages(context: ConfigInstantiationContext) : SavedConfig(context) {
    fun homeSet(home: Home) = getMessage("home-set").tags(home)

    fun homeDeleted(home: Home) = getMessage("home-deleted").tags(home)

    fun homeTeleported(home: Home) = getMessage("home-teleported").tags(home)

    fun homeNotFound(name: String) = getMessage("home-not-found").tagUnparsed("home", name)

    val homeNoneSet get() = getMessage("home-none-set")

    fun homeLimitReached(limit: Int) = getMessage("home-limit-reached").tagUnparsed("limit", limit.toString())

    val homeList get() = getMessage("home-list")
    val homeListEntry get() = getMessage("home-list-entry")
    val homeListSeparator get() = getMessage("home-list-separator")

    fun homeInvalidName(regex: String) = getMessage("home-invalid-name").tagParsed("regex", regex)

    fun homeWorldNotLoaded(worldName: String) = getMessage("home-world-not-loaded").tagUnparsed("world", worldName)
}
