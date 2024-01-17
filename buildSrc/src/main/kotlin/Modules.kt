val moduleNameRegex = Regex("[a-z0-9_-]+")

fun isValidModuleName(name: String?): Boolean {
    return name?.matches(moduleNameRegex) ?: false
}
