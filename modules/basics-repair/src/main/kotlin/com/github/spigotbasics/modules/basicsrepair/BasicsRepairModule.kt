package com.github.spigotbasics.modules.basicsrepair

import com.github.spigotbasics.core.command.parsed.arguments.SelectorSinglePlayerArg
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext

class BasicsRepairModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    val msgRepairHandOther: Message
        get() = messages.getMessage("repair-hand-other")

    val msgRepairHandSelf: Message
        get() = messages.getMessage("repair-hand-self")

    val msgRepairAllOther: Message
        get() = messages.getMessage("repair-all-other")

    val msgRepairAllSelf: Message
        get() = messages.getMessage("repair-all-self")

    val msgNothingToRepair: Message get() = messages.getMessage("repair-no-item")

    private val permission =
        permissionManager.createSimplePermission("basics.repair", "Allows to repair your current items")
    val permissionAll = permissionManager.createSimplePermission("basics.repair.all", "Allows to repair all your items")
    val permissionOthers =
        permissionManager.createSimplePermission("basics.repair.others", "Allows to repair other players' items")

    override fun onEnable() {
        commandFactory.parsedCommandBuilder("repair", permission).mapContext {
            description("Repairs an Item")
            usage = "[player]"

            path {
                playerOnly()
            }

            path {
                arguments {
                    permissions(permissionOthers)
                    named("player", SelectorSinglePlayerArg("player"))
                }
            }
        }.executor(CommandRepairHand(this)).register()

        commandFactory.parsedCommandBuilder("repairall", permissionAll).mapContext {
            description("Repairs all Items")
            usage = "[player]"

            path {
                playerOnly()
            }

            path {
                arguments {
                    permissions(permissionOthers)
                    named("player", SelectorSinglePlayerArg("player"))
                }
            }
        }.executor(CommandRepairAll(this)).register()
    }

    override fun reloadConfig() {
        config.reload()
        messages.reload()
    }
}
