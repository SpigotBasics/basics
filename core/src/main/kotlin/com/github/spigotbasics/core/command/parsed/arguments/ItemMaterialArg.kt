package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Dictionary
import com.github.spigotbasics.core.extensions.partialMatches
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import org.bukkit.Material

class ItemMaterialArg(name: String) : CommandArgument<Material>(name) {
    companion object {
        private val logger = BasicsLoggerFactory.getCoreLogger(ItemMaterialArg::class)

        private val materials = Dictionary.from(Material.entries.filter { it.isItem }.map { it.name to it })
        private val materialNames = materials.keys.toList().sorted()

        init {
            logger.info("${materials.size} items loaded from Bukkit's Material class.")
        }
    }

    override fun parse(value: String): Material? {
        return materials[value]
    }

    override fun tabComplete(typing: String): List<String> {
        return materialNames.partialMatches(typing)
    }
}
