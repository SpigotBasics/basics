package com.github.spigotbasics.core.command.parsed

import org.bukkit.Material

object ArgumentMaterial : Argument<Material> {
    override fun parse(value: String): Material? {
        return Material.getMaterial(value)
    }
}
