package com.github.spigotbasics.core.config

class CoreConfig(context: ConfigInstantiationContext) : SavedConfig(context) {
    val hideCommandsWhenNoPermission: Boolean
        get() = getBoolean("mask-no-permission-with-command-not-found")
}
