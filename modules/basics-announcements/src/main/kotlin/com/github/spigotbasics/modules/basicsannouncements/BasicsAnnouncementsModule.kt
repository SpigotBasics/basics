package com.github.spigotbasics.modules.basicsannouncements

import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.ModuleInstantiationContext
import java.util.concurrent.ThreadLocalRandom

class BasicsAnnouncementsModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private val interval
        get() = config.getLong("interval")

    private val pickRandom
        get() = config.getBoolean("pick-random")

    private val messages
        get() = config.getStringList("messages")

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
        announcerTaskId = scheduler.runTimer(0L, interval * 20L, this::broadcastAnnouncement)
    }

    private fun broadcastAnnouncement() {
        // TODO: Reimplement
//        if (messages.isEmpty()) return
//        if (pickRandom) msgIndex = localRandom.nextInt(messages.size)
//        val message = messages[msgIndex]
//        audience.players().sendMessage(MiniMessage.miniMessage().deserialize(message, *tagResolverFactory.getTagResolvers().toTypedArray()))
//        msgIndex = (msgIndex + 1).mod(messages.size)
    }
}