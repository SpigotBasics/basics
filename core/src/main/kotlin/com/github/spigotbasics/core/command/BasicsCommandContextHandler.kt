package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.command.raw.RawCommandContext
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.Permission

abstract class BasicsCommandContextHandler(
//    val coreMessages: CoreMessages,
//    val messageFactory: MessageFactory,
) {
    companion object {
        @Throws(BasicsCommandException::class)
        fun requirePlayer(name: String): Player {
            val player = Bukkit.getPlayer(name)
            if (player == null) {
                throw BasicsCommandException(CommandResult.playerNotFound(name))
            }
            return player
        }

        @Throws(BasicsCommandException::class)
        fun requirePlayer(sender: CommandSender): Player {
            if (sender !is Player) {
                throw CommandResult.NOT_FROM_CONSOLE.asException()
            }
            return sender
        }

        @Throws(BasicsCommandException::class)
        fun notFromConsole(sender: CommandSender): Player {
            if (sender !is Player) {
                throw CommandResult.NOT_FROM_CONSOLE.asException()
            }
            return sender
        }

        // TODO: In 99% of cases, we should just use requirePlayer(CommandSender) instead of this method
        @Throws(BasicsCommandException::class)
        fun requirePlayerOrMustSpecifyPlayerFromConsole(sender: CommandSender): Player {
            val player = sender as? Player
            if (player == null) {
                throw CommandResult.MUST_BE_PLAYER_OR_SPECIFY_PLAYER_FROM_CONSOLE.asException()
            }
            return player
        }

        @Throws(BasicsCommandException::class)
        fun failIfFlagsLeft(context: RawCommandContext) {
            if (context.flags.isEmpty()) return
            throw CommandResult.unknownFlag(context.flags[0]).asException()
        }

        @Throws(BasicsCommandException::class)
        fun failInvalidArgument(argument: String): CommandResult {
            throw CommandResult.invalidArgument(argument).asException()
        }

        fun requirePermission(
            sender: CommandSender,
            permission: Permission,
        ) {
            if (!sender.hasPermission(permission)) {
                throw CommandResult.noPermission(permission).asException()
            }
        }

        // @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

        fun requireItemInHand(player: Player): ItemStack {
            val item = player.inventory.itemInMainHand
            if (item.type.isAir) {
                throw CommandResult.NO_ITEM_IN_HAND.asException()
            }
            return item
        }

        fun requireItemInHandOther(player: Player): ItemStack {
            val item = player.inventory.itemInMainHand
            if (item.type.isAir) {
                throw CommandResult.noItemInHandOthers(player).asException()
            }
            return item
        }
    }
}
