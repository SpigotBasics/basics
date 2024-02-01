package com.github.spigotbasics.core.messages.tags

import com.github.spigotbasics.common.Either
import com.github.spigotbasics.core.extensions.genitiveSuffix
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.pipe.SpigotPaperFacade
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.util.logging.Level

/**
 * Allows easy construction of custom tag resolvers for MiniMessage
 */
class TagResolverFactory(private val facade: SpigotPaperFacade) {

    private val logger = BasicsLoggerFactory.getCoreLogger(TagResolverFactory::class)
    private val miniMessage = MiniMessage.miniMessage()

    //private var customTagsMap: Map<String, String> = emptyMap()
    private var customTagResolvers: List<TagResolver> = listOf()
    private var defaultNonPlayerTagResolverList: List<TagResolver> = listOf();

    private fun createDefaultNonPlayerTagResolverList(): List<TagResolver> {
        return listOf(
            //Placeholder.parsed("basics", "<bold>Basics Plugin</bold>")
        )
    }

    private fun createDefaultPlayerTagResolverList(player: Player): List<TagResolver> {
        return listOf(
            Placeholder.parsed("player-name", player.name),
            Placeholder.parsed("player-name-genitive-suffix", player.name.genitiveSuffix()),
            createDisplayNameTagResolver(player)
        )
    }

    private fun createDisplayNameTagResolver(player: Player): TagResolver {
        val result = facade.getDisplayName(player)
        return if (result is Either.Right) {
            Placeholder.component("player-display-name", miniMessage.deserialize(result.value.value))
        } else if (result is Either.Left) {
            Placeholder.unparsed("player-display-name", result.value)
        } else {
            error("Unknown result type: ${result::class.qualifiedName}")
        }
    }

    /**
     * Gets all applicable tag resolvers
     *
     * @param player The player to be used for player-related placeholders
     * @return List of all tag resolvers
     */
    fun getTagResolvers(player: Player? = null): List<TagResolver> {
        val list = mutableListOf<TagResolver>()
        list.addAll(defaultNonPlayerTagResolverList) // TODO:
        list.addAll(customTagResolvers) // TODO: Cache these two
        if (player != null) {
            list.addAll(createDefaultPlayerTagResolverList(player))
        }
        return list
    }

    /**
     * Loads all custom tags from the given YAML configuration
     *
     * @param yaml The YAML configuration
     */
    fun loadCustomTags(yaml: YamlConfiguration) {
        try {
//            val simplePlaceholders = yaml.getValues(true).mapValues {
//                val value: String = it.value?.toString() ?: ""
//                return@mapValues value
//            }


            customTagResolvers = yaml.getValues(false).mapNotNull { (key, value) ->
                try {
                    val tag = CustomTag.parse(key, value)
                    return@mapNotNull tag.toTagResolver()
                } catch (e: Exception) {
                    e.message?.let { logger.warning(it) } ?: logger.warning("Failed to parse tag '$key'")
                    return@mapNotNull null
                }
            }.toList()

            //customTagsMap = map
            //customParsedTagResolvers = simplePlaceholders.map { Placeholder.parsed(it.key, it.value) }
            defaultNonPlayerTagResolverList = createDefaultNonPlayerTagResolverList()

        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load custom tags", e)
        }
    }

}