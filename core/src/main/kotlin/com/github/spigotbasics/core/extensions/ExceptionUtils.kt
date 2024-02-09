package com.github.spigotbasics.core.extensions

import kotlin.reflect.KClass

inline fun <R> (() -> R).multiCatch(
    vararg classes: KClass<out Throwable>,
    thenDo: () -> R,
): R {
    return try {
        this()
    } catch (ex: Throwable) {
        if (ex::class in classes) thenDo() else throw ex
    }
}

inline fun <R> Throwable.multiCatch(
    vararg classes: KClass<out Throwable>,
    thenDo: () -> R,
): R {
    if (classes.any { this::class.java.isAssignableFrom(it.java) }) {
        return thenDo()
    } else {
        throw this
    }
}

private val builtinShortenedPackages =
    mapOf(
        "com.github.spigotbasics.core" to "c.g.s.c",
        "com.github.spigotbasics.pipe" to "c.g.s.pipe",
        "com.github.spigotbasics.modules" to "c.g.s.m",
        "com.github.spigotbasics.plugin" to "c.g.s.pl",
        "com.github.spigotbasics" to "c.g.s",
        "org.bukkit.craftbukkit" to "o.b.c",
        "org.bukkit" to "o.b",
        "net.minecraft.server" to "n.m.s",
        "net.minecraft" to "n.m",
    )

private fun compactClassName(
    className: String,
    shortenedPackages: Map<String, String>,
): String {
    val entry = shortenedPackages.entries.find { className.startsWith(it.key) }
    return entry?.let {
        className.replaceFirst(it.key, it.value)
    } ?: className
}

fun Throwable.toCompactStackTrace(
    shortenedPackages: Map<String, String> = emptyMap(),
    maxLines: Int = 10,
): String {
    val compactStackTrace = StringBuilder()

    run loop@{
        this.stackTrace.forEachIndexed { index, element ->
            val fullClassName = compactClassName(element.className, builtinShortenedPackages + shortenedPackages)
            val packageName = fullClassName.substringBeforeLast(".")
            val className = fullClassName.substringAfterLast(".")
            compactStackTrace.append(
                "<gray>$packageName.</gray>" +
                    "<gold>$className</gold>" +
                    "<gray>#</gray>" +
                    element.methodName +
                    "<gray>(</gray>" +
                    element.lineNumber +
                    "<gray>)</gray>\n",
            )
            if (index == maxLines - 1 && this.stackTrace.size > maxLines) {
                compactStackTrace.append("<gray>... ${this.stackTrace.size - maxLines} more</gray>\n")
                return@loop
            }
        }
    }

    return compactStackTrace.toString()
}
