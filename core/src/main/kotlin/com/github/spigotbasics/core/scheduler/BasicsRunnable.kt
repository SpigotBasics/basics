package com.github.spigotbasics.core.scheduler

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

// TODO: Move this use FoliaLib
internal class BasicsRunnable(
    private val plugin: Plugin,
    private val taskIds: MutableSet<Int>,
    private val task: () -> Unit,
) : BukkitRunnable() {
    override fun run() {
        task.invoke()
    }

    override fun cancel() {
        super.cancel()
        taskIds.remove(taskId)
    }

    fun runTask(): Int {
        runTask(plugin)
        taskIds.add(taskId)
        return taskId
    }

    fun runTaskAsync(): Int {
        runTaskAsynchronously(plugin)
        taskIds.add(taskId)
        return taskId
    }

    fun runTaskLater(delay: Long): Int {
        runTaskLater(plugin, delay)
        taskIds.add(taskId)
        return taskId
    }

    fun runTaskLaterAsync(delay: Long): Int {
        runTaskLaterAsynchronously(plugin, delay)
        taskIds.add(taskId)
        return taskId
    }

    fun runTaskTimer(
        delay: Long,
        period: Long,
    ): Int {
        runTaskTimer(plugin, delay, period)
        taskIds.add(taskId)
        return taskId
    }

    fun runTaskTimerAsync(
        delay: Long,
        period: Long,
    ): Int {
        runTaskTimerAsynchronously(plugin, delay, period)
        taskIds.add(taskId)
        return taskId
    }
}
