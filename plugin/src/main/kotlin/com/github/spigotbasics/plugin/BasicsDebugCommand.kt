package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import com.github.spigotbasics.core.util.DurationParser
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor

class BasicsDebugCommand(private val plugin: BasicsPluginImpl) : TabExecutor {

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): MutableList<String> {
        if (args.size == 1) {
            return listOf("start", "stop", "resendcommands").partialMatches(args.get(0))
        } else {
            return mutableListOf()
        }
    }

    private val tasks: MutableMap<CommandSender, Int> = mutableMapOf()
    private val scheduler = BasicsScheduler(plugin)

    fun startTask(sender: CommandSender, delay: Long) {
        stopTask(sender)
        val taskId = scheduler.runTimer(0, delay) {
            showForSender(sender)
        }
        tasks[sender] = taskId
    }

    private fun stopTask(sender: CommandSender) {
        tasks[sender]?.let {
            scheduler.kill(it)
            tasks.remove(sender)
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        when (args.size) {
            0 -> {
                showForSender(sender)
                return true
            }

            1 -> return when (args[0].lowercase()) {
                "start" -> {
                    startTask(sender, 20)
                    true
                }

                "stop" -> {
                    stopTask(sender)
                    true
                }

                "resendcommands" -> {
                    for(player in plugin.server.onlinePlayers) {
                        player.updateCommands()
                    }
                    true
                }

                else -> false
            }

            2 -> return if (args[0] == "start") {
                startTask(sender, DurationParser.parseDurationToTicks(args[1]))
                true
            } else false
        }
        return false
    }

    private fun showForSender(sender: CommandSender) {
        val cachedLoginData = plugin.modulePlayerDataLoader.cachedLoginData
        val scheduledClearCacheFutures = plugin.modulePlayerDataLoader.scheduledClearCacheFutures

        val output = """

PlayerDataListener:
  -             cached login data: <cached-login-data>
  - scheduled clear cache futures: <scheduled-clear-cache-futures>

"""

        val message = plugin.messageFactory.createMessage(output)
            .tagMessage("cached-login-data", shouldBe(cachedLoginData.size, 0))
            .tagMessage(
                "scheduled-clear-cache-futures",
                shouldBe(scheduledClearCacheFutures.size, cachedLoginData.size)
            )

        message.sendToSender(sender)
    }

    fun <T : Any> shouldBe(value: T, expected: T): Message {
        return plugin.messageFactory.createMessage(
            if (value == expected) {
                "<green>OK $value</green>"
            } else {
                "<red>!! $value (should be $expected)</red>"
            }
        )
    }
}