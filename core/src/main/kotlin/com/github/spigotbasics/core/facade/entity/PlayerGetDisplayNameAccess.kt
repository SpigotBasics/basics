package com.github.spigotbasics.core.facade.entity

import com.github.spigotbasics.core.Either
import com.github.spigotbasics.core.facade.SimpleMethodAggregator
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

object PlayerGetDisplayNameAccess : SimpleMethodAggregator<Player, Unit, Unit, Component, String>(
    Player::class,
    "displayName", null,
    "getDisplayName", null
) {
    // TODO: Move get method one interface / class up
    fun get(player: Player): Either<Component, String> {
        return apply(player, null, null)
    }
}