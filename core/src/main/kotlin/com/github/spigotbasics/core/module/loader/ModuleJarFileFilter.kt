package com.github.spigotbasics.core.module.loader

import java.io.File
import java.io.FileFilter

object ModuleJarFileFilter : FileFilter {
    override fun accept(pathname: File?): Boolean {
        return pathname?.name?.lowercase()?.endsWith(".jar") ?: false
    }
}