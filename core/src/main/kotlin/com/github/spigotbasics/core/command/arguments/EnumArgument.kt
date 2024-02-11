package com.github.spigotbasics.core.command.arguments

import com.github.spigotbasics.common.Dictionary

class EnumArgument<T : Enum<T>>(val enumClass: Class<T>) : ArgumentType<T> {

    private val enumConstants: Dictionary<T>

    init {
        enumConstants = Dictionary()
        for (constant in enumClass.enumConstants) {
            enumConstants[constant.name] = constant
        }
    }
    override fun parse(value: String): T {
        return enumConstants[value] ?: throw EnumValueNotFoundException(value, enumClass)
    }
}
