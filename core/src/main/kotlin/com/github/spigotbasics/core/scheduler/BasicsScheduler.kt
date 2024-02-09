package com.github.spigotbasics.core.scheduler

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

// TODO: Make this use FoliaLib
class BasicsScheduler(private val plugin: Plugin) {
    private val taskIds: MutableSet<Int> = HashSet()

    fun runTask(task: () -> Unit): Int {
        return runnable(task).runTask()
    }

    fun runTaskAsync(task: () -> Unit): Int {
        return runnable(task).runTaskAsync()
    }

    fun runLater(
        delay: Long,
        task: () -> Unit,
    ): Int {
        return runnable(task).runTaskLater(delay)
    }

    fun runLaterAsync(
        delay: Long,
        task: () -> Unit,
    ): Int {
        return runnable(task).runTaskLaterAsync(delay)
    }

    fun runTimer(
        delay: Long,
        period: Long,
        task: () -> Unit,
    ): Int {
        return runnable(task).runTaskTimer(delay, period)
    }

    fun runTimerAsync(
        delay: Long,
        period: Long,
        task: () -> Unit,
    ): Int {
        return runnable(task).runTaskTimerAsync(delay, period)
    }

    fun kill(id: Int) {
        Bukkit.getScheduler().cancelTask(id)
    }

    fun killAll() {
        taskIds.forEach(Bukkit.getScheduler()::cancelTask)
    }

    private fun runnable(task: () -> Unit): BasicsRunnable {
        return BasicsRunnable(plugin, taskIds, task)
    }
}
