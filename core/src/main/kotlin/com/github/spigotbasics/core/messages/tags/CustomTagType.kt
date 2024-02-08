package com.github.spigotbasics.core.messages.tags

/**
 * Represents the type of custom tag
 */
enum class CustomTagType {
    /**
     * Parsed tags can contain further tags
     */
    PARSED,

    /**
     * Unparsed tags are like plain text
     */
    UNPARSED,

    /**
     * Color codes that can be used with <cute-pink>opening and closing tags</cute-pink>
     */
    COLOR,

//    /**
//     * Placeholder tags are placeholders from PlaceholderAPI
//     */
//    PLACEHOLDER;

    ;

    companion object {
        /**
         * Returns the tag type from the given string, case-insensitive
         *
         * @param string The string to be converted
         * @return The tag type
         * @throws IllegalArgumentException If the string is not a valid tag type
         */
        @Throws(IllegalArgumentException::class)
        fun fromString(string: String): CustomTagType {
            return when (string.uppercase()) {
                "PARSED" -> PARSED
                "UNPARSED" -> UNPARSED
                "COLOR" -> COLOR
                // "PAPI", "PLACEHOLDER", "PLACEHOLDERAPI" -> PLACEHOLDER
                else -> throw IllegalArgumentException("Unknown tag type '$string' - valid types are ${entries.joinToString()}")
            }
        }
    }
}
