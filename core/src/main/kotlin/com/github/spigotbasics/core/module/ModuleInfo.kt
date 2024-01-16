package com.github.spigotbasics.core.module

data class ModuleInfo(
    /**
     * Module's main class, must implement [BasicsModule]
     */
    val mainClass: String,
    /**
     * Module's name. Names of enabled plugins must be unique.
     */
    val name: String,
    /**
     * Module's version
     */
    val version: String
) {


    /**
     * Name and version
     */
    val nameAndVersion = "$name v$version"

}