package com.github.spigotbasics.core.command.common

import com.github.spigotbasics.core.messages.Message
import org.bukkit.permissions.Permission

// TODO: createCommandInfo in BasicsModule
data class CommandInfo(
    val name: String,
    val permission: Permission,
    val permissionMessage: Message,
    val description: String?,
    val usage: String,
    val aliases: List<String>,
)
