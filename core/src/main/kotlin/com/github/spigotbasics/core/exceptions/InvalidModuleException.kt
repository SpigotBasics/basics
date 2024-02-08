package com.github.spigotbasics.core.exceptions

/**
 * Thrown when attempting to load an invalid module file
 *
 * @constructor Create empty Invalid module exception
 */
class InvalidModuleException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
