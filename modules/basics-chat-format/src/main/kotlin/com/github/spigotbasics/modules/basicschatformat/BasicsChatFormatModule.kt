package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import com.github.spigotbasics.modules.basicschatformat.commmands.ColorChatCommand
import com.github.spigotbasics.modules.basicschatformat.data.ChatData
import org.bukkit.event.EventPriority
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

private fun String.escapeFormat(): String {
    return this.replace("%", "%%")
}

class BasicsChatFormatModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    private var storage: NamespacedStorage? = null
    private val chatData = mutableMapOf<UUID, ChatData>()
    override val messages = getConfig(ConfigName.MESSAGES, Messages::class.java)

    val permissionChatColor =
        permissionManager.createSimplePermission("basics.chatcolor", "Allows access to the /chatcolor command")

    val format: Message
        get() = config.getMessage("chat-format")

    val messageColor: String get() = config.getString("default-message-color") ?: "white"

    val regex: Regex
        get() =
            Regex(
                config.getString(
                    "regex-whitelist",
                    config.getString(
                        "regex-blacklist",
                        "\\b(bold|b|italic|em|i|underlined|u|strikethrough|st|obfuscated|obf)\\b",
                    )!!,
                )!!,
            )

    override fun onEnable() {
        storage = createStorage()

        if (Spiper.isPaper) {
            eventBus.subscribe(PaperChatEventListener(this))
        } else {
            eventBus.subscribe(AsyncPlayerChatEvent::class.java, this::changeChatFormatSpigot, EventPriority.HIGHEST)
        }

        commandFactory.rawCommandBuilder("chatcolor", permissionChatColor)
            .description("Sets your chat color")
            .usage("[reset|<color>]")
            .executor(ColorChatCommand(this))
            .register()
    }

    override fun reloadConfig() {
        config.reload()
        messages.reload()
    }

    fun changeChatFormatSpigot(event: AsyncPlayerChatEvent) {
        event.format =
            format
                .concerns(event.player)
                .tagUnparsed("message", event.message) // TODO: To allow MiniMessage in chat, this should be parsed.
                .tagParsed("message-color", "<${getChatDataOrDefault(event.player.uniqueId).color}>")
                .toLegacyString().escapeFormat()
    }

    fun getChatDataOrDefault(uuid: UUID) = chatData.getOrDefault(uuid, ChatData(messageColor))

    fun setChatData(
        uuid: UUID,
        chatData: ChatData,
    ) {
        this.chatData[uuid] = chatData
    }

    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> =
        CompletableFuture.runAsync {
            loadPlayerDataBlocking(uuid)?.let { chatData[uuid] = it }
        }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        val chatDatum = chatData[uuid]
        val storage = storage ?: error("Storage is null")
        return storage.setJsonElement(uuid.toString(), Serialization.toJson(chatDatum))
    }

    override fun forgetPlayerData(uuid: UUID) {
        chatData.remove(uuid)
    }

    private fun loadPlayerDataBlocking(uuid: UUID): ChatData? {
        val storage = storage ?: error("Storage is null")
        try {
            val json = storage.getJsonElement(uuid.toString()).join() ?: return null
            return Serialization.fromJson(json, ChatData::class.java)
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load chat data for $uuid", e)
        }

        return null
    }

    fun resetChatColor(uniqueId: UUID) {
        forgetPlayerData(uniqueId)
    }
}
