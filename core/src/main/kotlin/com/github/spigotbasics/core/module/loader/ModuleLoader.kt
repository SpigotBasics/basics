package com.github.spigotbasics.core.module.loader

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.Constants.MODULE_YML_FILE_NAME
import com.github.spigotbasics.core.exceptions.InvalidModuleException
import com.github.spigotbasics.core.logger.BasicsLoggerFactory
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.core.module.ModuleInfo
import org.bukkit.Server
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class ModuleLoader
    @Throws(InvalidModuleException::class)
    constructor(
        private val plugin: BasicsPlugin,
        private val server: Server,
        val file: File,
    ) : AutoCloseable {
        private val path: String = file.absolutePath
        private val logger = BasicsLoggerFactory.getCoreLogger(ModuleLoader::class)

        // val jarFile: JarFile
        val info: ModuleInfo
        private val classLoader: ModuleJarClassLoader
        private val bMainClass: Class<out BasicsModule> by lazy {
            try {
                getMainClass()
            } catch (e: InvalidModuleException) {
                throw InvalidModuleException("Failed to get main class", e)
            }
        }

        init {
            if (!file.exists()) {
                throw InvalidModuleException("Module file $path does not exist")
            }
            if (!file.isFile) {
                throw InvalidModuleException("Module file $path is not a file")
            }
            if (!file.name.lowercase().endsWith(".jar")) {
                throw InvalidModuleException("Module file $path is not a jar file")
                // logger.warning("Module file $path is not a jar file - trying to load it anyway...")
            }

//        try {
//             JarFile(file).use { jarFile ->

            classLoader =
                try {
                    ModuleJarClassLoader(file, javaClass.classLoader)
                } catch (e: Exception) {
                    throw InvalidModuleException("Failed to create class loader for module file $path", e)
                }

            val moduleInfoUrl =
                classLoader.getResource(MODULE_YML_FILE_NAME)
                    ?: throw InvalidModuleException("Module file $path does not contain a $MODULE_YML_FILE_NAME file")

            val moduleInfoYaml = YamlConfiguration()

            try {
                val moduleInfoYamlString = moduleInfoUrl.readText()
                moduleInfoYaml.loadFromString(moduleInfoYamlString)
            } catch (e: InvalidConfigurationException) {
                throw InvalidModuleException("Failed to parse $MODULE_YML_FILE_NAME file in $path to YamlConfiguration", e)
            }

            try {
                info = ModuleInfo.fromYaml(moduleInfoYaml)
            } catch (e: InvalidModuleException) {
                throw InvalidModuleException("Failed to create ModuleInfo for $MODULE_YML_FILE_NAME", e)
            }
        }

        @Throws(InvalidModuleException::class)
        fun getMainClass(): Class<out BasicsModule> {
            val mainClassName = info.mainClass
            val mainClass =
                try {
                    // Class.forName(mainClassName)
                    classLoader.loadClass(mainClassName)
                } catch (e: ClassNotFoundException) {
                    throw InvalidModuleException("Main class $mainClassName not found", e)
                } catch (e: ExceptionInInitializerError) {
                    throw InvalidModuleException("Failed to initialize main class $mainClassName", e)
                } catch (e: LinkageError) {
                    throw InvalidModuleException("Failed to link main class $mainClassName", e)
                }
            if (!BasicsModule::class.java.isAssignableFrom(mainClass)) {
                throw InvalidModuleException("Main class $mainClassName does not implement BasicsModule")
            }

            return mainClass.asSubclass(BasicsModule::class.java)
        }

        @Throws(InvalidModuleException::class)
        fun createInstance(): BasicsModule {
            val mainClassName = info.mainClass

            val constructor =
                try {
                    bMainClass.getConstructor(ModuleInstantiationContext::class.java)
                } catch (e: NoSuchMethodException) {
                    throw InvalidModuleException(
                        "Main class $mainClassName does not have a constructor with a single parameter of type ModuleInstantiationContext",
                        e,
                    )
                }

            val moduleInstantiationContext =
                try {
                    ModuleInstantiationContext(
                        plugin = plugin,
                        server = server,
                        info = info,
                        file = file,
                        classLoader = classLoader,
                    )
                } catch (e: Exception) {
                    throw InvalidModuleException("Failed to create ModuleInstantiationContext for main class $mainClassName", e)
                }

            val moduleInstance =
                try {
                    constructor.newInstance(moduleInstantiationContext)
                } catch (e: Exception) {
                    throw InvalidModuleException("Failed to instantiate main class $mainClassName", e)
                }

            return moduleInstance
        }

        override fun close() {
            classLoader.close()
        }
    }
