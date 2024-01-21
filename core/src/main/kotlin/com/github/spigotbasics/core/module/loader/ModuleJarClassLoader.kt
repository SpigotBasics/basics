package com.github.spigotbasics.core.module.loader

import com.github.spigotbasics.core.module.ForbiddenFruitException
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.net.URLClassLoader

class ModuleJarClassLoader(val file: File, parentLoader: ClassLoader) :
    URLClassLoader(arrayOf(file.toURI().toURL()), parentLoader) {

    companion object {
        val FORBIDDEN_CLASSES = listOf(BukkitScheduler::class.java).map { it.name }
    }

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        //println("Loading class $name (resolve: $resolve)")
        if (FORBIDDEN_CLASSES.contains(name)) {
            throw ForbiddenFruitException(name)
        }
        return super.loadClass(name, resolve)
    }
}
