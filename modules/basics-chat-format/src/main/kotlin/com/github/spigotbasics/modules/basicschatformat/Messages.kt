package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.config.ConfigInstantiationContext
import com.github.spigotbasics.core.config.SavedConfig

class Messages(context: ConfigInstantiationContext) : SavedConfig(context) {
    fun colorSet(color: String) =
        getMessage("color-set")
            .tagParsed("selected-color-parsed", "<$color>")
            .tagUnparsed("selected-color-name", color)

    fun colorInvalid(color: String) =
        getMessage("color-invalid")
            .tagUnparsed("selected-color-name", color)
}
