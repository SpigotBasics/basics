package com.github.spigotbasics.core.module

data class ModuleInfo(val mainClass: String, val name: String, val version: String) {
    val nameAndVersion = "$name v$version"
}