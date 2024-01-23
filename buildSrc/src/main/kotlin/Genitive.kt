fun String.genitiveForm(): String {
    return when {
        this.endsWith("s") -> this + "'"
        this.endsWith("x") -> this + "'"
        this.endsWith("z") -> this + "'"
        else -> this + "'s"
    }
}