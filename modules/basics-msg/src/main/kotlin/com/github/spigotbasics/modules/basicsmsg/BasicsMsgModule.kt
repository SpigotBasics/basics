package com.github.spigotbasics.modules.basicsmsg

import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import org.bukkit.permissions.PermissionDefault

class BasicsMsgModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {

    val formatReceived: Message
        get() = config.getMessage("format-received")

    val formatSent: Message
        get() = config.getMessage("format-sent")

    val formatSelf: Message
        get() = config.getMessage("format-self")

    val formatConsole: Message
        get() = config.getMessage("format-console")

    val permission = permissionManager.createSimplePermission(
        "basics.msg",
        "Allows the player to send private messages",
        PermissionDefault.TRUE
    )

    override fun onEnable() {
        createCommand().name("msg")
            .permission(permission)
            .description("Sends a private message to another player")
            .usage("/msg <player> <message>")
            .executor(MsgExecutor(this))
            .register()
    }

}