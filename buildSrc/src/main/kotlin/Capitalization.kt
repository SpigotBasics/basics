private val SPLIT = Regex("[-_\\s]+")

fun String.pascalCase(): String {
    return this.split(SPLIT).joinToString("") { it.lowercase().replaceFirstChar { it.uppercaseChar() } }
}

fun String.camelCase(): String {
    return pascalCase().replaceFirstChar { it.lowercaseChar() }
}