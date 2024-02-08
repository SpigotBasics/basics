package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.command.CommandResult
import com.github.spigotbasics.core.command.TabCompleter
import com.github.spigotbasics.core.extensions.startsWithIgnoreCase
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.util.StringUtil

class RepairCommand(private val module: BasicsRepairModule) : BasicsCommandExecutor(module) {

    override fun execute(context: BasicsCommandContext): CommandResult {
        context.readFlags()
        val args = context.args
        val repairAll = context.popFlag("--all")

        if(repairAll) {
            requirePermission(context.sender, module.permissionAll)
        }

        val target = if(args.size > 0) {
            requirePlayer(context.sender, args[0])
        } else {
            requirePlayerOrMustSpecifyPlayerFromConsole(context.sender)
        }

        if(target != context.sender) {
            requirePermission(context.sender, module.permissionOthers)
        }

        if(repairAll) {
            if(target == context.sender) {
                runAllSelf(target)
            } else {
                runAllOther(context.sender, target)
            }
        } else {
            requireItemInHand(target)
            if(target == context.sender) {
                runHandSelf(target)
            } else {
                runHandOther(context.sender, target)
            }
        }

        return CommandResult.SUCCESS
    }

    override fun tabComplete(context: BasicsCommandContext): MutableList<String>? {
        val args = context.args
        val sender = context.sender
        val mayAll = sender.hasPermission(module.permissionAll)
        val mayOthers = sender.hasPermission(module.permissionOthers)

        if(!mayAll && !mayOthers) return mutableListOf()
        if(args.size == 1) {
            if(mayAll && !mayOthers) {
                return StringUtil.copyPartialMatches(args[0], listOf("--all"), mutableListOf())
            } else if(mayAll && mayOthers) {
                return StringUtil.copyPartialMatches(args[0], listOf("--all") + TabCompleter.getPlayers(sender, args[0]), mutableListOf())
            } else if (!mayAll && mayOthers) {
                return TabCompleter.getPlayers(sender, args[0])
            }
        } else if(args.size == 2) {
            if(mayAll && mayOthers && args[0].startsWithIgnoreCase("--")) {
                return TabCompleter.getPlayers(sender, args[1])
            }
        }
        return mutableListOf()
    }

    fun runHandSelf(player: Player) {
        repairHand(player)
        module.msgRepairHandSelf.concerns(player).sendToPlayer(player)
    }


    fun runHandOther(sender: CommandSender, player: Player) {
        repairHand(player)
        module.msgRepairHandOther.concerns(player.player).sendToSender(sender)
    }


    fun runAllSelf(player: Player) {
        repairAll(player)
        module.msgRepairAllSelf.concerns(player).sendToPlayer(player)
    }

    fun runAllOther(sender: CommandSender, player: Player) {
        repairAll(player)
        module.msgRepairAllOther.concerns(player.player).sendToSender(sender)
    }

    private fun repairHand(target: Player) {
        repairItem(target.inventory.itemInMainHand)
    }

    private fun repairAll(target: Player) {
        for (content in target.inventory.contents) {
            repairItem(content)
        }
    }

    private fun repairItem(item: ItemStack?) {
        if (item == null || item.type.isAir) return
        val meta = item.itemMeta
        if(meta is Damageable) {
            if (meta.hasDamage()) {
                meta.damage = 0
                item.setItemMeta(meta)
            }
        }
    }



}
