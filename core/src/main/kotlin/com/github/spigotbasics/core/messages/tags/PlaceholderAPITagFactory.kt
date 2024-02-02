package com.github.spigotbasics.core.messages.tags

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.minimessage.Context
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object PlaceholderAPITagFactory {

    fun getPapiTagResolver(): TagResolver {
        return TagResolver.resolver("click-by-version") { args, context ->
            val version = args.popOr("version expected").value()
            Tag.styling(ClickEvent.openUrl("https://jd.advntr.dev/api/$version/"))
        }

    }

}