package com.github.spigotbasics.core.messages.tags

import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object CustomTagToTagResolverFactory {
    fun createTagResolverForCustomTag(tag: CustomTag): TagResolver {
        return tag.convert()
    }

    private fun CustomTag.convert(): TagResolver {
        return when (type) {
            CustomTagType.PARSED -> Placeholder.parsed(name, value)
            CustomTagType.UNPARSED -> Placeholder.unparsed(name, value)
            CustomTagType.COLOR -> createColorTagResolver()
        }
    }

    private fun CustomTag.createColorTagResolver(): TagResolver {
        if (CustomTag.hexRegex.matches(value)) {
            val color = TextColor.fromHexString(value)!!
            return Placeholder.styling(name, color)
        } else {
            throw IllegalArgumentException("Invalid hex color for tag '$name': $value - must be in format #RRGGBB")
        }
    }
}
