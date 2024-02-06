package com.github.spigotbasics.modules.basicsworkbench

import com.github.spigotbasics.core.command.BasicsCommandContext
import com.github.spigotbasics.core.command.BasicsCommandExecutor
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import org.bukkit.entity.Player

class BasicsWorkbenchModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    private val permission = permissionManager.createSimplePermission(
        "basics.workbench",
        "Allows the player to open a workbench"
    )

    override fun onEnable() {
        createCommand("workbench", permission)
            .description("Opens a workbench")
            .aliases(listOf("craft"))
            .usage("/workbench")
            .executor(WorkbenchExecutor())
            .register()
    }

    inner class WorkbenchExecutor : BasicsCommandExecutor(this@BasicsWorkbenchModule) {
        override fun execute(context: BasicsCommandContext): Boolean {
            val player = notFromConsole(context.sender)
            player.openWorkbench(null, true)
            return true
        }
    }

}