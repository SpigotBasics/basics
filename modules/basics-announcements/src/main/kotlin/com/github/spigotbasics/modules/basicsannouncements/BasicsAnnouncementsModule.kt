package com.github.spigotbasics.modules.basicsannouncements

import com.github.spigotbasics.core.extensions.getDurationAsTicks
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import java.util.concurrent.ThreadLocalRandom

class BasicsAnnouncementsModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val interval
        get() = config.getDurationAsTicks("interval", 60 * 20L)

    private val pickRandom
        get() = config.getBoolean("pick-random")

    private val messages
        get() = config.getStringList("messages")

    private val showInConsole
        get() = config.getBoolean("show-in-console")

    private val localRandom = ThreadLocalRandom.current()

    private var msgIndex = 0
    private var announcerTaskId = 0

    override fun onEnable() {
        scheduleAnnouncementTask()
    }

    override fun reloadConfig() {
        super.reloadConfig()
        scheduler.kill(announcerTaskId)
        scheduleAnnouncementTask()
    }

    private fun scheduleAnnouncementTask() {
        msgIndex = 0
        announcerTaskId = scheduler.runTimer(0L, interval, this::broadcastAnnouncement)
    }

    private fun broadcastAnnouncement() {
        if (messages.isEmpty()) return
        if (pickRandom) msgIndex = localRandom.nextInt(messages.size)
        val message = messageFactory.createMessage(messages[msgIndex])
        msgIndex = (msgIndex + 1).mod(messages.size)

        if (showInConsole) message.sendToConsole()
        message.sendToAllPlayers()
    }
}
