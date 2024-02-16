package com.github.spigotbasics.core.messages

import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import com.github.spigotbasics.core.messages.tags.TagResolverFactory
import com.github.spigotbasics.pipe.SerializedMiniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * Represents a message containing of zero or more lines
 *
 * See [com.github.spigotbasics.core.config.SavedConfig.getMessage] on how to obtain a message
 *
 * @property lines Lines of the message
 * @constructor Create a new message
 */
data class Message(
    var lines: List<String>,
    val audienceProvider: AudienceProvider,
    var tagResolverFactory: TagResolverFactory,
    var concerns: Player? = null,
    val customTagResolvers: MutableList<TagResolver> = mutableListOf(),
) {
    private val audiences by lazy { audienceProvider.audience }

    fun sendToPlayer(player: Player) {
        audiences.player(player).sendMessage(toAdventureComponent())
    }

    fun sendToPlayerActionBar(player: Player) {
        audiences.player(player).sendActionBar(toAdventureComponent())
    }

    fun sendToAllPlayers() {
        audiences.players().sendMessage(toAdventureComponent())
    }

    fun sendToConsole() {
        audiences.console().sendMessage(toAdventureComponent())
    }

    fun sendToSender(sender: CommandSender) {
        audiences.sender(sender).sendMessage(toAdventureComponent())
    }

    fun sendToPlayers(players: Collection<Player>) {
        players.forEach {
            sendToPlayer(it)
        }
    }

    fun concerns(player: Player?): Message {
        this.concerns = player
        return this
    }

    /**
     * Applies the given transform to each line of the message
     *
     * @param transform Transform to apply
     * @return The message with the transform applied
     */
    fun map(transform: (String) -> String): Message {
        lines = lines.map(transform)
        return this
    }

    private fun tags(tags: Collection<TagResolver>): Message {
        customTagResolvers.addAll(tags)
        return this
    }

    fun tags(`object`: MessageTagProvider): Message {
        `object`.getMessageTags().forEach {
            tags(it.toTagResolver())
        }
        `object`.getTagProviders().forEach {
            when(it) {
                is TagResolver -> tags(it)
                else -> {
                    if(it is CustomTag) {
                        error("CustomTags must be provided by the MessageTagProvider.getMessageTags method, not the MessageTagProvider.getTagProviders method.")
                    } else {
                        error("Unsupported TagResolver type: ${it::class} - must be instance of ${TagResolver::class}")
                    }
                }
            }
        }
        return this
    }

    fun tagUnparsed(
        tag: String,
        value: String,
    ): Message {
        return tags(tagResolverFactory.createMessageSpecificPlaceholderUnparsed(tag, value))
    }

    fun tagParsed(
        tag: String,
        value: String,
    ): Message {
        return tags(tagResolverFactory.createMessageSpecificPlaceholderParsed(tag, value))
    }

    fun tagMessage(
        tag: String,
        value: Message,
    ): Message {
        return tags(tagResolverFactory.createMessageSpecificPlaceholderMessage(tag, value))
    }

    fun tagMiniMessage(
        tag: String,
        value: SerializedMiniMessage,
    ): Message {
        return tags(
            tagResolverFactory.createMessageSpecificPlaceholderComponent(
                tag,
                miniMessage.deserialize(value.value),
            ),
        )
    }

    @Deprecated("Use tagMessage or tagParsed", ReplaceWith("tagMessage(tag, value)"))
    fun tags(vararg tags: Pair<String, Any>): Message {
        @Suppress("DEPRECATION")
        return tags(tags.map { (key, value) -> toPlaceholder(key, value) })
    }

    @Deprecated("Use tagMessage or tagParsed", ReplaceWith("tagMessage(tag, value)"))
    fun tags(tags: Map<String, String>): Message {
        return tags(
            tags.map { (key, value) ->
                tagResolverFactory.createMessageSpecificPlaceholderUnparsed(
                    key,
                    value,
                )
            },
        )
    }

    @Deprecated("Use tagMessage or tagParsed", ReplaceWith("tagMessage(tag, value)"))
    private fun toPlaceholder(
        key: String,
        value: Any,
    ): TagResolver {
        if (value is String) {
            return tagResolverFactory.createMessageSpecificPlaceholderUnparsed(key, value)
        }
        if (value is Message) {
            return tagResolverFactory.createMessageSpecificPlaceholderMessage(key, value)
        }
        if (value is SerializedMiniMessage) {
            return tagResolverFactory.createMessageSpecificPlaceholderComponent(
                key,
                miniMessage.deserialize(value.value),
            )
        }
        error("Unsupported Placeholder value type: ${value::class}")
    }

    private fun tags(vararg tags: TagResolver): Message {
        return tags(tags.toList())
    }

    private fun getAllTagResolvers(): Array<TagResolver> {
        val defaultResolvers = tagResolverFactory.getTagResolvers(concerns)
        return (defaultResolvers + customTagResolvers).toTypedArray()
    }

    internal fun toAdventureComponents(): List<Component> {
        return lines.map { miniMessage.deserialize(it, *getAllTagResolvers()) }
    }

    internal fun toAdventureComponent(): Component {
        return Component.join(JoinConfiguration.newlines(), toAdventureComponents())
    }

    companion object {
        private val miniMessage = MiniMessage.miniMessage()

        private val legacyComponentSerializer =
            LegacyComponentSerializer
                .builder()
                .hexColors()
                .useUnusualXRepeatedCharacterHexFormat() // They are not unusual (+1)
                .build()

        private val bungeeComponentSerializer = BungeeComponentSerializer.get()
    }

    fun serialize(): SerializedMiniMessage {
        return SerializedMiniMessage(miniMessage.serialize(toAdventureComponent()))
    }

    fun toLegacyString(): String {
        return legacyComponentSerializer.serialize(toAdventureComponent())
    }

    fun toBungeeComponents(): Array<net.md_5.bungee.api.chat.BaseComponent> {
        return bungeeComponentSerializer.serialize(toAdventureComponent())
    }

    override fun toString(): String {
        return ChatColor.stripColor(toLegacyString())
    }
}
