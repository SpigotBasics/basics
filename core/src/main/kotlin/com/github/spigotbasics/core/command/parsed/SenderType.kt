package com.github.spigotbasics.core.command.parsed

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.KClass

abstract class SenderType<T : CommandSender>(val requiredType: KClass<T>) {
    override fun toString() = requiredType.simpleName ?: "Unknown"
}

object PlayerSender : SenderType<Player>(Player::class)

object AnySender : SenderType<CommandSender>(CommandSender::class)
