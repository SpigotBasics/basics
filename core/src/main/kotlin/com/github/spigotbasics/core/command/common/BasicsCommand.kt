package com.github.spigotbasics.core.command.common

import com.github.spigotbasics.core.command.raw.RawCommandContext
import com.github.spigotbasics.core.command.raw.RawTabCompleter
import com.github.spigotbasics.core.config.CoreConfig
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.CoreMessages
import com.github.spigotbasics.core.messages.MessageFactory
import com.github.spigotbasics.pipe.exceptions.UnsupportedServerSoftwareException
import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import java.util.logging.Level

/**
 * Represents a registered command. This is what is actually registered to Bukkit.
 *
 * @property info Command info
 * @property executor Command executor
 */
class BasicsCommand internal constructor(
    var info: CommandInfo,
    private var executor: BasicsCommandExecutor?,
    private var tabCompleter: RawTabCompleter?,
    coreConfig: CoreConfig,
    val coreMessages: CoreMessages,
    val messageFactory: MessageFactory,
) : Command(info.name) {
    private val logger = BasicsLoggerFactory.getCoreLogger(BasicsCommand::class)

    init {
        val permString = info.permission.name
        permission = if (coreConfig.hideCommandsWhenNoPermission) permString else null
        description = info.description ?: ""
        usage = info.usage
        permissionMessage = info.permissionMessage.tagParsed("permission", permString).toLegacyString()
    }

    override fun execute(
        sender: CommandSender,
        commandLabel: String,
        origArgs: Array<out String>,
    ): Boolean {
        if (!sender.hasPermission(info.permission)) {
            coreMessages.noPermission
                .apply { if (sender is Player) concerns(sender) }
                .tagParsed("permission", info.permission.name)
                .sendToSender(sender)
            return true
        }

        val args = origArgs.toMutableList()

        val context =
            RawCommandContext(
                sender = sender,
                command = this,
                label = commandLabel,
                args = args,
                location = if (sender is Entity) sender.location else null,
            )

        if (executor == null) {
            coreMessages.commandModuleDisabled.sendToSender(sender)
            return true
        }

        val returned: CommandResult?
        try {
            returned = executor!!.execute(context)

            try {
                returned?.process(context)
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Error processing returned command result for ${info.name}", e)
            }
        } catch (e: BasicsCommandException) {
            try {
                e.commandResult.process(context)
            } catch (e: Exception) {
                logger.log(Level.SEVERE, "Error processing thrown command result for ${info.name}", e)
            }
        } catch (e: UnsupportedServerSoftwareException) {
            // Also want to catch NoSuchMethod and NoSuchField exception here
            coreMessages.unsupportedServerSoftware(e.feature).sendToSender(sender)
        } catch (e: Throwable) {
            coreMessages.errorExecutingCommand(sender, e).sendToSender(sender)
            logger.log(Level.SEVERE, "Error executing command ${info.name}", e)
        }
        return true
    }

    internal fun replaceCommand(command: BasicsCommand) {
        this.executor = command.executor
        this.info = command.info
    }

    override fun tabComplete(
        sender: CommandSender,
        alias: String,
        args: Array<out String>,
        location: Location?,
    ): List<String> {
        if (!sender.hasPermission(info.permission)) return mutableListOf()

        val context =
            RawCommandContext(
                sender = sender,
                command = this,
                label = alias,
                args = args.toMutableList(),
                location = location,
            )
        if (tabCompleter == null) return mutableListOf()
        val result = tabCompleter!!.tabComplete(context)
        return result ?: super.tabComplete(sender, alias, args, location)
    }

    /**
     * Disable this command's executor by setting it to null. Disabled commands will always return true and
     * print a message to the sender.
     *
     */
    fun disableExecutor() {
        executor = null
        tabCompleter = null
    }
}
