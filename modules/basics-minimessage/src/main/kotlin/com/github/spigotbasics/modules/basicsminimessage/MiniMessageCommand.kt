package com.github.spigotbasics.modules.basicsminimessage

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import com.github.spigotbasics.core.minimessage.TagResolverFactory
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("minimessage")
@CommandPermission("basics.minimessage")
class MiniMessageCommand(private val tagResolverFactory: TagResolverFactory, private val audience: BukkitAudiences) : BaseCommand() {

    private val miniMessage = MiniMessage.miniMessage()

    @Default
    fun message(sender: CommandSender, text: Array<String>) {
        val joined = text.joinToString(separator = " ")
        val player = sender as? Player
        audience.all().sendMessage(miniMessage.deserialize(joined, *tagResolverFactory.getTagResolvers(player).toTypedArray()))
    }

}
