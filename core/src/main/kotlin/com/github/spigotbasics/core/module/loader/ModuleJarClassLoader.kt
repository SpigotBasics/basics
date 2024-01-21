package com.github.spigotbasics.core.module.loader

import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ForbiddenFruitException
import com.github.spigotbasics.core.module.ModuleInfo
import com.github.spigotbasics.core.scheduler.BasicsScheduler
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.net.URLClassLoader

class ModuleJarClassLoader(val file: File, parentLoader: ClassLoader) :
    URLClassLoader(arrayOf(file.toURI().toURL()), parentLoader) {

    companion object {
        val FORBIDDEN_CLASSES = mapOf(

            BukkitScheduler::class to BasicsModule::scheduler,
            BukkitRunnable::class to BasicsModule::scheduler


        ).map { (forbidden, replacement) ->
            forbidden.java.name to replacement
        }.toMap()
    }

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        val replacement = FORBIDDEN_CLASSES[name]
        if(replacement != null) {
            throw ForbiddenFruitException(name, replacement)
        }
        return super.loadClass(name, resolve)
    }
}
