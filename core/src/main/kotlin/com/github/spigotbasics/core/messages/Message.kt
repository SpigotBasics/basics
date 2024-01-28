package com.github.spigotbasics.core.messages

import com.github.spigotbasics.pipe.SerializedMiniMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
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
    //val miniMessage: MiniMessage,
    var tagResolverFactory: TagResolverFactory?,
    var concerns: Player? = null,
    val customTagResolvers: MutableList<TagResolver> = mutableListOf()
) {

    private val audiences by lazy { audienceProvider.audience }

    fun sendToPlayer(player: Player) {
        audiences.player(player).sendMessage(toComponent())
    }

    fun sendToAllPlayers() {
        audiences.players().sendMessage(toComponent())
    }

    fun sendToConsole() {
        audiences.console().sendMessage(toComponent())
    }

    fun sendToSender(sender: CommandSender) {
        audiences.sender(sender).sendMessage(toComponent())
    }

    fun sendToPlayers(players: Collection<Player>) {
        players.forEach() {
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

    fun tags(vararg tags: Pair<String, Any>): Message {
        return tags(tags.map { (key, value) -> toPlaceholder(key, value) })
    }

    fun tags(tags: Map<String, String>): Message {
        return tags(tags.map { (key, value) -> Placeholder.unparsed(key, value) })
    }

    private fun toPlaceholder(key: String, value: Any): TagResolver {
        if(value is String) {
            return Placeholder.unparsed(key, value)
        }
        if(value is Message) {
            return Placeholder.component(key, value.toComponent())
        }
        if(value is SerializedMiniMessage) {
            return Placeholder.component(key, miniMessage.deserialize(value.value))
        }
        error("Unsupported Placeholder value type: ${value::class}")
    }

    private fun tags(vararg tags: TagResolver): Message {
        return tags(tags.toList())
    }

//    /**
//     * Parses the message from MiniMessage format to Components and sends them to the given audience
//     *
//     * @param receiver Audience to send the message to
//     */
//    fun sendTo(receiver: Audience) {
//        receiver.sendMessage(toComponent())
//    }

    private fun getAllTagResolvers(): Array<TagResolver> {
        val defaultResolvers = tagResolverFactory?.getTagResolvers(concerns) ?: emptyList()
        return (defaultResolvers + customTagResolvers).toTypedArray()
    }


    private fun toComponents(): List<Component> {
        return lines.map { miniMessage.deserialize(it, *getAllTagResolvers()) }
    }

    private fun toComponent(): Component {
        return Component.join(JoinConfiguration.newlines(), toComponents())
    }

    companion object {
        private val miniMessage = MiniMessage.miniMessage()

        private val legacyComponentSerializer = LegacyComponentSerializer
            .builder()
            .useUnusualXRepeatedCharacterHexFormat() // They are not unusual
            .build()
    }

    fun serialize(): SerializedMiniMessage {
        return SerializedMiniMessage(miniMessage.serialize(toComponent()))
    }

    fun toLegacy(): String {
        return legacyComponentSerializer.serialize(toComponent())
    }


}