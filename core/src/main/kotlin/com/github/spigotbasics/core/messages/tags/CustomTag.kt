package com.github.spigotbasics.core.messages.tags

import net.kyori.adventure.text.format.StyleBuilderApplicable
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.configuration.ConfigurationSection

data class CustomTag(val name: String, val value: String, val type: CustomTagType) {
    companion object {
        val defaultTagType = CustomTagType.PARSED

        val hexRegex = Regex("#[0-9a-fA-F]{6}")

        /**
         * Parses a tag from the given... whatever it is.
         *
         * @param key The tag key
         * @param value The tag value
         * @return The parsed tag
         */
        fun parse(key: String, value: Any): CustomTag {
            @Suppress("UNCHECKED_CAST")
            return when (value) {
                is String -> fromString(key, value)
                is ConfigurationSection -> fromSection(key, value)
                is Map<*, *> -> fromMap(key, value as Map<String, Any?>)
                else -> throw IllegalArgumentException("Invalid tag format for tag '$key': $value")
            }
        }

        /**
         * Parses a tag from the given string
         *
         * @param key The tag key
         * @param value The tag value
         */
        fun fromString(key: String, value: String) = CustomTag(key, value, defaultTagType)

        /**
         * Parses a tag from the given configuration section
         *
         * @param key The tag key
         * @param value The tag value
         */
        fun fromSection(key: String, value: ConfigurationSection) = fromMap(key, value.getValues(false))

        /**
         * Parses a tag from the given map
         *
         * @param key The tag key
         * @param map The tag value
         * @return The parsed tag
         */
        fun fromMap(key: String, map: Map<String, Any?>): CustomTag {
            val type = (map["type"] as? String)?.let { CustomTagType.fromString(it) } ?: defaultTagType
            val value = when (map["value"]) {
                is String -> map["value"] as String
                is List<*> -> (map["value"] as List<*>).joinToString("\n")
                else -> throw IllegalArgumentException("Invalid value for tag '$key': ${map["value"]}")
            }
            return CustomTag(key, value, type)
        }

    }

    fun toTagResolver(): TagResolver {
        return CustomTagToTagResolverFactory.createTagResolverForCustomTag(this)
    }




}