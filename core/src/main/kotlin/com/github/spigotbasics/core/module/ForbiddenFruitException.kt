package com.github.spigotbasics.core.module

import com.github.spigotbasics.core.scheduler.BasicsScheduler
import kotlin.reflect.KProperty1

class ForbiddenFruitException(clazzName: String, replacement: KProperty1<BasicsModule, BasicsScheduler>) :
    Exception("Module tried to access forbidden class \"$clazzName\" - use \"${replacement}\" instead") {
}