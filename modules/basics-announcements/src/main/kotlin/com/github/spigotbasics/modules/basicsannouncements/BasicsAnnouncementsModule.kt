package com.github.spigotbasics.modules.basicsannouncements

import com.github.spigotbasics.common.RandomList
import com.github.spigotbasics.core.extensions.getDurationAsTicks
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import java.util.concurrent.ThreadLocalRandom

class BasicsAnnouncementsModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val interval
        get() = config.getDurationAsTicks("interval", 60 * 20L)

    private val pickRandom
        get() = config.getBoolean("pick-random")

    private var broadcasts = createList()

    private fun createList(): RandomList<String> {
        val list = config.getStringList("messages")
        return RandomList(list, 0.5)
    }

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
        broadcasts = createList()
    }

    private fun scheduleAnnouncementTask() {
        msgIndex = 0
        announcerTaskId = scheduler.runTimer(0L, interval, this::broadcastAnnouncement)
    }

    private fun broadcastAnnouncement() {
        if (broadcasts.isEmpty()) return

        val broadcast =
            when (pickRandom) {
                true -> broadcasts.getRandom()
                false -> broadcasts[msgIndex.also { msgIndex = (msgIndex + 1) % broadcasts.size }]
            }

        broadcast?.let {
            val message = messageFactory.createMessage(it)
            if (showInConsole) message.sendToConsole()
            message.sendToAllPlayers()
        }
    }
}
