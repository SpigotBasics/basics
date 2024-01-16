package com.github.spigotbasics.plugin

import com.github.spigotbasics.core.BasicsPlugin
import com.github.spigotbasics.core.Either
import com.github.spigotbasics.core.extensions.placeholders
import com.github.spigotbasics.core.module.BasicsModule
import com.github.spigotbasics.modules.test.TestModule
import org.bukkit.plugin.java.JavaPlugin
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createType
import kotlin.reflect.full.valueParameters

class BasicsPluginImpl : JavaPlugin(), BasicsPlugin {
    override val availableModules: MutableList<KClass<out BasicsModule>> = ArrayList()
    override val enabledModules: MutableList<BasicsModule> = ArrayList()

    override fun loadModule(clazz: KClass<out BasicsModule>): Either<BasicsModule, Exception> {
        availableModules.add(clazz)
        val module: BasicsModule
        try {
            module = createModule(clazz)
        } catch (exception: Exception) {
            logger.severe("Could not load module class ${clazz.qualifiedName}")
            return Either.Right(exception)
        }
        try {
            module.enable()
            enabledModules.add(module)
        } catch (exception: Exception) {
            logger.severe("Could not enable module ${module.name}")
            return Either.Right(exception)
        }

        return Either.Left(module)
    }

    private fun createModule(clazz: KClass<out BasicsModule>): BasicsModule {
        val constructor = clazz.constructors.stream()
            .filter { it.parameters.size == 1 && it.parameters[0].type == BasicsPlugin::class.createType() }
            .findFirst()
            .orElseThrow {
                IllegalArgumentException("Cannot find constructor for BasicsModule ${clazz.qualifiedName}")
            }
        return constructor.call(this)


//        var foundConstructor: KFunction<BasicsModule>? = null
//        for (constructor in clazz.constructors) {
//            println("Constructor: ${constructor}, parameters = ${constructor.parameters}, typeParameters = ${constructor.typeParameters}, valueParameters = ${constructor.valueParameters}")
//            foundConstructor = constructor
//        }
//        return foundConstructor?.call(this) ?: throw IllegalStateException("Cannot find constructor for ${clazz.qualifiedName}")
    }

    override fun onEnable() {
        logger.info(
            "Basics v%version% enabled! This plugin was written by %authors%."
                .placeholders(
                    "version" to description.version,
                    "authors" to "cool people"
                )
        )

        enableDefaultModules()
    }

    private fun enableDefaultModules() {
        loadModule(TestModule::class)
    }
}