package com.github.spigotbasics.common

/**
 * Represents a comparable Minecraft version
 *
 * @property major Major version - 1 for 1.20.4
 * @property minor Minor version - 20 for 1.20.4
 * @property patch Patch version - 4 for 1.20.4, or 0 if not specified
 */
data class MinecraftVersion(
    val major: Int,
    val minor: Int,
    val patch: Int = 0,
) : Comparable<MinecraftVersion> {
    companion object {
        val v1_20_6 = MinecraftVersion(1, 20, 6)
        val v1_20_5 = MinecraftVersion(1, 20, 5)
        val v1_20_4 = MinecraftVersion(1, 20, 4)
        val v1_20_3 = MinecraftVersion(1, 20, 3)

        /**
         * Gets a MinecraftVersion from the output of [org.bukkit.Bukkit.getBukkitVersion] or the version string of the
         * bukkit artifact, e.g. "1.20.4-R0.1-SNAPSHOT"
         *
         * @param version Bukkit Version, e.g. "1.20.4-R0.1-SNAPSHOT"
         * @return MinecraftVersion, or [UNKNOWN] if it couldn't be parsed
         */
        fun fromBukkitVersion(version: String): MinecraftVersion {
            try {
                val split = version.split("-")[0].split(".")
                val major = split[0].toInt()
                val minor = split[1].toInt()
                val patch = if (split.size > 2) split[2].toInt() else 0
                return MinecraftVersion(major, minor, patch)
            } catch (e: Exception) {
                // logger.log(Level.SEVERE, "Failed to parse Bukkit version $version", e)
                return UNKNOWN
            }
        }

        val UNKNOWN = MinecraftVersion(0, 0, 0)
    }

    override operator fun compareTo(other: MinecraftVersion): Int {
        if (major > other.major) {
            return 1
        } else if (major < other.major) {
            return -1
        } else {
            return if (minor > other.minor) {
                1
            } else if (minor < other.minor) {
                -1
            } else {
                if (patch > other.patch) {
                    1
                } else if (patch < other.patch) {
                    -1
                } else {
                    0
                }
            }
        }
    }

    override fun toString(): String {
        return "$major.$minor.$patch"
    }
}
