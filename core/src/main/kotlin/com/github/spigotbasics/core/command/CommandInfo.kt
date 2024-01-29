package com.github.spigotbasics.core.command

import com.github.spigotbasics.core.messages.Message
import org.bukkit.permissions.Permission

// TODO: Executor does not belong into CommandInfo
// TODO: createCommandInfo in BasicsModule
// TODO: Permission message not shown when permission is missing, but "no command found" - make this configurable?
//       That'd require not setting any permission on the actual Command, but only checking it in the executor (BasicsCommand#execute)
data class CommandInfo (
    val name: String,
    val permission: Permission,
    val permissionMessage: Message,
    val description: String?,
    val usage: String?,
    val aliases: List<String>,
    //val executor: (CommandSender, BasicsCommand, String, List<String>) -> Boolean
    //val executor: BasicsCommandExecutor
) {



}
