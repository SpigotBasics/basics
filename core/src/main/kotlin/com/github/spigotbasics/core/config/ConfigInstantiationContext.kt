package com.github.spigotbasics.core.config

import com.github.spigotbasics.core.messages.MessageFactory
import java.io.File

data class ConfigInstantiationContext constructor(
    val file: File,
    val dataFolder: File,
    val messageFactory: MessageFactory
)
