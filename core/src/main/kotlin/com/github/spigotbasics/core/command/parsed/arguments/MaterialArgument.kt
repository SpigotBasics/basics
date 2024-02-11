package com.github.spigotbasics.core.command.parsed.arguments

import com.github.spigotbasics.common.Dictionary
import com.github.spigotbasics.core.command.parsed.CommandArgument
import com.github.spigotbasics.core.extensions.partialMatches
import org.bukkit.Material

class MaterialArgument(name: String) : CommandArgument<Material>(name) {
    private val materials = Dictionary.from(Material.entries.filter { it.isItem }.map { it.name to it })
    private val materialNames = materials.keys.toList().sorted()

    override fun parse(value: String): Material? {
        return materials[value]
    }

    override fun tabComplete(typing: String): List<String> {
        return materialNames.partialMatches(typing)
    }
}
