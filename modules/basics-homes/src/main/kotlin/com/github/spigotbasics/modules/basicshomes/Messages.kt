package com.github.spigotbasics.modules.basicshomes

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig
import com.github.spigotbasics.modules.basicshomes.data.Home

class Messages(context: ConfigInstantiationContext) : SavedConfig(context) {
    val homeNotFound get() = getMessage("home-not-found")
    val homeList get() = getMessage("home-list")
    val homeListEntry get() = getMessage("home-list-entry")
    val homeListSeparator get() = getMessage("home-list-separator")
    val homeNoneSet get() = getMessage("home-none-set")

    fun homeTeleport(home: Home) = getMessage("home-teleport").tags(home)

    fun homeSet(home: Home) = getMessage("home-set").tags(home)

    fun homeRemoved(home: Home) = getMessage("home-remove").tags(home)

    fun homeLimitReached(limit: Int) = getMessage("home-limit-reached").tagParsed("limit", limit.toString())

    fun homeRegexViolated(regex: String) = getMessage("home-invalid-name").tagParsed("regex", regex)
}
