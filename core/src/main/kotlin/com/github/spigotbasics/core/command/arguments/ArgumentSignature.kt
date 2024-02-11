package com.github.spigotbasics.core.command.arguments

import org.bukkit.command.CommandSender

class ArgumentSignature<T: CommandSender>(val requiredSender: Class<out T>, val arguments: List<ArgumentType<*>>)
