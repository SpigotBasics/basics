package com.github.spigotbasics.core.logger

import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleInfo
import com.github.spigotbasics.core.storage.StorageType
import org.bukkit.Bukkit
import java.io.File
import kotlin.reflect.KClass


object BasicsLoggerFactory {

    fun getLogger(name: String): BasicsLogger {
        return BasicsLogger(Bukkit.getLogger(),"Basics/$name")
    }

    fun getModuleLogger(module: ModuleInfo): BasicsLogger {
        return getLogger("Module/${module.name}")
    }

    fun getModuleLogger(module: BasicsModule, clazz: KClass<*>): BasicsLogger {
        return getLogger("Module/${module.info.name}/${clazz.simpleName}")
    }

    fun getConfigLogger(file: File): BasicsLogger {
        return getLogger("Config/${file.name}")
    }

    fun getCoreLogger(clazz: KClass<*>): BasicsLogger {
        if (clazz.java.classLoader != javaClass.classLoader) {
            throw IllegalArgumentException("Class must be loaded by the same classloader as this class")
        }
        return getLogger("Core/${clazz.simpleName}")
    }

    fun getStorageLogger(type: StorageType, namespace: String): BasicsLogger {
        return getLogger("Storage/${type.name}/$namespace")

    }

}