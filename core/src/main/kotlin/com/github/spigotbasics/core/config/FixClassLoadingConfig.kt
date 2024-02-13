package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.logger.BasicsLoggerFactory

class FixClassLoadingConfig(context: ConfigInstantiationContext) : SavedConfig(context) {
    private val logger = BasicsLoggerFactory.getCoreLogger(this::class)

    val setEnabledDuringOnDisable
        get() = getBooleanAndLog("set-enabled-during-ondisable")

    val loadAllClasses
        get() = getBooleanAndLog("load-all-classes")

    val abuseClassesList: List<String>
        get() = getStringList("abuse-classes.others")

    val abuseClassBasicsModule
        get() = getBooleanAndLog("abuse-classes.BasicsModule")

    val abuseClassAbstractBasicsModule
        get() = getBooleanAndLog("abuse-classes.AbstractBasicsModule")

    val abuseClassModuleManager
        get() = getBooleanAndLog("abuse-classes.ModuleManager")

    val callIteratorOnEmptyList
        get() = getBooleanAndLog("call-iterator-on-empty-list")

    private fun getBooleanAndLog(key: String): Boolean {
        val value = getBoolean(key)
        if (!value) {
            logger.warning("$key is set to false")
        }
        return value
    }
}
