package com.github.spigotbasics.core.command.arguments

class PlayerNotFoundException(value: String) : IllegalArgumentException("Player not found: $value")
