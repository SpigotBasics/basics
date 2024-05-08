package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig

class Messages(context: ConfigInstantiationContext) : SavedConfig(context) {
    fun colorSet(
        colorParsed: String,
        colorRaw: String,
    ) = getMessage("color-set")
        .tagParsed("selected-color-parsed", colorParsed)
        .tagUnparsed("selected-color-name", colorRaw)

    fun colorInvalid(color: String) =
        getMessage("color-invalid")
            .tagUnparsed("selected-color-name", color)

    val colorReset get() = getMessage("color-reset")
}
