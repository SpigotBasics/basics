package com.github.spigotbasics.modules.basicscore

import com.github.spigotbasics.core.messages.tags.CustomTag
import com.github.spigotbasics.core.messages.tags.MessageTagProvider
import com.github.spigotbasics.core.module.BasicsModule

class ModuleTagProvider(private val module: BasicsModule) : MessageTagProvider {

    override fun getMessageTags(): List<CustomTag> {
        return listOf(
            CustomTag.parsed("module", module.info.name),
            CustomTag.parsed("version", module.info.version),
            CustomTag.parsed("module_description", module.info.description.joinToString("\n")),
        )
    }
}