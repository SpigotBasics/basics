package com.github.spigotbasics.core.messages.tags

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.extensions.genitiveSuffix
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.messages.Message
import com.github.spigotbasics.pipe.SpigotPaperFacade
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.UUID
import java.util.logging.Level

/**
 * Allows easy construction of custom tag resolvers for MiniMessage
 */
class TagResolverFactory(private val facade: SpigotPaperFacade) {
    private val logger = BasicsLoggerFactory.getCoreLogger(TagResolverFactory::class)
    private val miniMessage = MiniMessage.miniMessage()

    private val nonPlayerResolvers = mutableListOf<TagResolver>()
    private val playerResolvers = mutableMapOf<UUID, List<TagResolver>>()

    private fun createDefaultPlaceholders(): List<TagResolver> {
        return listOf(
            // Clashes with per-player PAPI, so only add this if requesting TagResolvers for non-player
            // PlaceholderAPITagFactory.nonPlayerPapi
        )
    }

    private fun createDefaultPlaceholdersForPlayer(player: Player): List<TagResolver> {
        return listOf(
            Placeholder.parsed("${MESSAGE_SPECIFIC_TAG_PREFIX}player-name", player.name),
            Placeholder.parsed("${MESSAGE_SPECIFIC_TAG_PREFIX}player-name-genitive-suffix", player.name.genitiveSuffix()),
            createDisplayNameTagResolver(player),
            PlaceholderAPITagFactory.playerPapi(player),
        )
    }

    private fun createDisplayNameTagResolver(player: Player): TagResolver {
        return when (val result = facade.getDisplayName(player)) {
            is Either.Right -> Placeholder.component("${MESSAGE_SPECIFIC_TAG_PREFIX}player-display-name", miniMessage.deserialize(result.value.value))
            is Either.Left -> Placeholder.unparsed("${MESSAGE_SPECIFIC_TAG_PREFIX}player-display-name", result.value)
            else -> error("Unknown result type: ${result::class.qualifiedName}")
        }
    }

    /**
     * Gets all applicable tag resolvers, which are:
     *
     * - The default non-player placeholders
     * - The custom tag resolvers (custom-tags.yml)
     * - The player-specific placeholders (player-name, display-name, ...), if a player is provided
     * - The player-specific PAPI Resolver, if a player is provided
     * - The non-player PAPI Resolver, if a player is not provided
     *
     * @param player The player to be used for player-related placeholders
     * @return List of all tag resolvers
     */
    fun getTagResolvers(player: Player? = null): List<TagResolver> {
        return if (player != null) {
            getPlayerResolvers(player)
        } else {
            nonPlayerResolvers + PlaceholderAPITagFactory.nonPlayerPapi
        }
    }

    // TODO: Use getConfig(...) instead of passing a YamlConfiguration

    /**
     * Loads and caches all TagResolvers, including default, custom and player-specific resolvers
     *
     * @param customTagsConfig Configuration for custom tags
     */
    fun loadAndCacheAllTagResolvers(customTagsConfig: YamlConfiguration) {
        try {
            val customTagResolvers =
                customTagsConfig.getValues(false).mapNotNull { (key, value) ->
                    try {
                        val tag = CustomTag.fromConfig(key, value)
                        return@mapNotNull tag.toTagResolver()
                    } catch (e: Exception) {
                        e.message?.let { logger.warning(it) } ?: logger.warning("Failed to parse tag '$key'")
                        return@mapNotNull null
                    }
                }.toList()

            nonPlayerResolvers.clear()
            nonPlayerResolvers.addAll(customTagResolvers)
            nonPlayerResolvers.addAll(createDefaultPlaceholders())

            playerResolvers.clear()
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load custom tags", e)
        }
    }

    /**
     * Listens to player join and quit events to manage the list of player-specific tag resolvers
     */
    inner class Listener : org.bukkit.event.Listener {
        @EventHandler
        fun onJoin(event: PlayerJoinEvent) {
            getPlayerResolvers(event.player)
        }

        @EventHandler
        fun onQuit(event: PlayerQuitEvent) {
            playerResolvers.remove(event.player.uniqueId)
        }
    }

    /**
     * Returns a list of ALL available TagResolvers for this player, which are:
     * - The default non-player placeholders
     * - The custom tag resolvers (custom-tags.yml)
     * - The player-specific placeholders (player-name, display-name, ...)
     * - The player-specific PAPI placeholders
     *
     * The list is cached for each player, or generated if not cached.
     *
     * @param player The player to get the resolvers for
     * @return The list of resolvers
     */
    private fun getPlayerResolvers(player: Player): List<TagResolver> {
        return playerResolvers.computeIfAbsent(player.uniqueId) {
            val list = mutableListOf<TagResolver>()
            list.addAll(createDefaultPlaceholdersForPlayer(player))
            list.addAll(nonPlayerResolvers)
            return@computeIfAbsent list
        }
    }

    internal fun createMessageSpecificPlaceholderUnparsed(
        key: String,
        value: String,
    ): TagResolver {
        return Placeholder.unparsed(MESSAGE_SPECIFIC_TAG_PREFIX + key, value)
    }

    internal fun createMessageSpecificPlaceholderParsed(
        key: String,
        value: String,
    ): TagResolver {
        return Placeholder.parsed(MESSAGE_SPECIFIC_TAG_PREFIX + key, value)
    }

    internal fun createMessageSpecificPlaceholderMessage(
        key: String,
        value: Message,
    ): TagResolver {
        return Placeholder.component(MESSAGE_SPECIFIC_TAG_PREFIX + key, value.toAdventureComponent())
    }

    internal fun createMessageSpecificPlaceholderComponent(
        key: String,
        value: Component,
    ): TagResolver {
        return Placeholder.component(MESSAGE_SPECIFIC_TAG_PREFIX + key, value)
    }
}
