package com.github.spigotbasics.modules.basicschatformat

import com.github.spigotbasics.core.Serialization
import com.github.spigotbasics.core.Spiper
import com.github.spigotbasics.core.command.parsed.arguments.AnyStringArg
import com.github.spigotbasics.core.config.ConfigName
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.core.module.AbstractBasicsModule
import com.github.spigotbasics.core.module.loader.ModuleInstantiationContext
import com.github.spigotbasics.core.storage.NamespacedStorage
import com.github.spigotbasics.modules.basicschatformat.data.ChatFormatData
import com.github.spigotbasics.modules.basicschatformat.data.ChatFormatStore
import com.github.spigotbasics.modules.basicschatformat.listener.PaperChatEventListener
import com.github.spigotbasics.modules.basicschatformat.listener.SpigotChatEventListener
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.logging.Level

class BasicsChatFormatModule(context: ModuleInstantiationContext) : AbstractBasicsModule(context) {
    companion object {
        lateinit var instance: BasicsChatFormatModule
    }

    private val permissionChatColor =
        permissionManager.createSimplePermission("basics.chatcolor", "Allows access to the /chatcolor command")

    val permissionNamedColors =
        permissionManager.createSimplePermission("basics.chatcolor.named", "Allows access to all named colors")
    val permissionHexColors =
        permissionManager.createSimplePermission("basics.chatcolor.hex", "Allows access to all hex colors")
    val permissionGradientNamed =
        permissionManager.createSimplePermission(
            "basics.chatcolor.gradient.named",
            "Allows access to gradient with named colors only",
        )
    val permissionGradientHex =
        permissionManager.createSimplePermission(
            "basics.chatcolor.gradient.hex",
            "Allows access to gradient with hex colors only",
        )

    val chatFormat: Message = config.getMessage("chat-format")
    val messageColor: String get() = config.getString("default-message-color") ?: "white"
    val chatFormatStore = ChatFormatStore(this)
    override val messages: Messages = getConfig(ConfigName.MESSAGES, Messages::class.java)

    private lateinit var storage: NamespacedStorage

    override fun onEnable() {
        instance = this
        storage = createStorage(name = "chat_format")

        if (Spiper.isPaper) {
            eventBus.subscribe(PaperChatEventListener(this))
        } else {
            eventBus.subscribe(
                SpigotChatEventListener(this),
            )
        }

        commandFactory.parsedCommandBuilder("chatcolor", permissionChatColor).mapContext {
            usage = "[reset|set] <color>"
            path {
                playerOnly()

                arguments {
                    sub("set")
                    named("color", AnyStringArg("color"))
                }
            }

            path {
                playerOnly()

                arguments {
                    sub("reset")
                }
            }
        }.executor(ChatColorCommand(this)).register()
    }

    override fun reloadConfig() {
        config.reload()
        messages.reload()
    }

    override fun loadPlayerData(uuid: UUID): CompletableFuture<Void?> =
        CompletableFuture.runAsync {
            loadPlayerDataBlocking(uuid)?.let { chatFormatStore.chatFormatData[uuid] = it }
        }

    override fun savePlayerData(uuid: UUID): CompletableFuture<Void?> {
        val chatDatum = chatFormatStore.chatFormatData[uuid]
        val storage = storage ?: error("Storage is null")
        return storage.setJsonElement(uuid.toString(), Serialization.toJson(chatDatum))
    }

    override fun forgetPlayerData(uuid: UUID) {
        chatFormatStore.chatFormatData.remove(uuid)
    }

    private fun loadPlayerDataBlocking(uuid: UUID): ChatFormatData? {
        val storage = storage
        try {
            val json = storage.getJsonElement(uuid.toString()).join() ?: return null
            return Serialization.fromJson(json, ChatFormatData::class.java)
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load chat data for $uuid", e)
        }

        return null
    }
}
