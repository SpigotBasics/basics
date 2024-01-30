package com.github.spigotbasics.core.messages

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

// TODO: Add PlaceholderAPI <papi:...> tag
class TagResolverFactory(private val facade: SpigotPaperFacade) {

    private val logger = BasicsLoggerFactory.getCoreLogger(TagResolverFactory::class)

    private var customTagsMap: Map<String, String> = emptyMap()
    private var customParsedTagResolvers: List<TagResolver> =
        listOf() // = customTagsMap.map { Placeholder.parsed(it.key, it.value) }
    private var defaultNonPlayerTagResolverList: List<TagResolver> =
        listOf(); // = createDefaultNonPlayerTagResolverList()

    private val mini = MiniMessage.miniMessage()

    private fun createDefaultNonPlayerTagResolverList(): List<TagResolver> {
        return listOf( // TODO: Add static placeholders
            Placeholder.parsed("basics", "<bold>Basics Plugin</bold>")
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
            Placeholder.component("player-display-name", mini.deserialize(result.value.value))
        } else if (result is Either.Left) {
            Placeholder.unparsed("player-display-name", result.value)
        } else {
            error("Unknown result type: ${result::class.qualifiedName}")
        }
    }

    fun getTagResolvers(player: Player? = null): List<TagResolver> {
        val list = mutableListOf<TagResolver>()
        list.addAll(defaultNonPlayerTagResolverList) // TODO:
        list.addAll(customParsedTagResolvers) // TODO: Cache these two
        if (player != null) {
            list.addAll(createDefaultPlayerTagResolverList(player))
        }
        return list
    }

    fun loadCustomTags(yaml: YamlConfiguration) {
        try {
            val map = yaml.getValues(true).mapValues {
                val value: String = it.value?.toString() ?: ""
                //println("tag <${it.key}> = $value")

                return@mapValues value
            }


            customTagsMap = map
            customParsedTagResolvers = customTagsMap.map { Placeholder.parsed(it.key, it.value) }
            defaultNonPlayerTagResolverList = createDefaultNonPlayerTagResolverList()

        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Failed to load custom tags", e)
        }
    }

}