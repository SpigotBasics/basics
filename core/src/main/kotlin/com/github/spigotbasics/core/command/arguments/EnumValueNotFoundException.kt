package com.github.spigotbasics.core.command.arguments

class EnumValueNotFoundException(value: String, enumClass: Class<*>) : NoSuchElementException(
    "Enum value not found: $value in enum class: $enumClass",
)
