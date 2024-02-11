package com.github.spigotbasics.core.command.parsed2

import org.bukkit.Material

class MaterialArgument : CommandArgument<Material>() {
    override fun parse(value: String): Material? {
        return Material.valueOf(value.uppercase())
    }
}