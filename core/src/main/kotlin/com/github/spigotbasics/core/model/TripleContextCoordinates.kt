package com.github.spigotbasics.core.model

import com.github.spigotbasics.core.extensions.toZeroWhenEmpty
import org.bukkit.Location

data class TripleContextCoordinates(
    val x: Double,
    val xRelative: Relativity,
    val y: Double,
    val yRelative: Relativity,
    val z: Double,
    val zRelative: Relativity,
    val yaw: Float = 0f,
    val yawRelative: Relativity = Relativity.RELATIVE_TO_FIRST,
    val pitch: Float = 0f,
    val pitchRelative: Relativity = Relativity.RELATIVE_TO_FIRST,
) {
    enum class Relativity {
        ABSOLUTE,
        RELATIVE_TO_FIRST,
        RELATIVE_TO_SECOND,
    }

    fun toLocation(
        first: Location,
        second: Location = first,
    ): Location {
        val nX = applyRelativity(xRelative, x, first.x, second.x)
        val nY = applyRelativity(yRelative, y, first.y, second.y)
        val nZ = applyRelativity(zRelative, z, first.z, second.z)
        val nYaw = applyRelativity(yawRelative, yaw, first.yaw, second.yaw)
        val nPitch = applyRelativity(pitchRelative, pitch, first.pitch, second.pitch)
        return Location(first.world, nX, nY, nZ, nYaw, nPitch)
    }

    companion object {
        fun parse(string: String): TripleContextCoordinates {
            val parts = string.split(" ")
            val (x, xRelative) = parseSingle(parts[0])
            val (y, yRelative) = parseSingle(parts[1])
            val (z, zRelative) = parseSingle(parts[2])

            val (yaw, yawRelative) =
                if (parts.size > 3) {
                    parseSingle(parts[3])
                } else {
                    Pair(0.0, Relativity.RELATIVE_TO_FIRST)
                }

            val (pitch, pitchRelative) =
                if (parts.size > 4) {
                    parseSingle(parts[4])
                } else {
                    Pair(0.0, Relativity.RELATIVE_TO_FIRST)
                }

            return TripleContextCoordinates(
                x,
                xRelative,
                y,
                yRelative,
                z,
                zRelative,
                yaw.toFloat(),
                yawRelative,
                pitch.toFloat(),
                pitchRelative,
            )
        }

        fun applyRelativity(
            relativity: Relativity,
            value: Double,
            first: Double,
            second: Double,
        ): Double {
            return when (relativity) {
                Relativity.ABSOLUTE -> value
                Relativity.RELATIVE_TO_FIRST -> first + value
                Relativity.RELATIVE_TO_SECOND -> second + value
            }
        }

        fun applyRelativity(
            relativity: Relativity,
            value: Float,
            first: Float,
            second: Float,
        ): Float {
            return when (relativity) {
                Relativity.ABSOLUTE -> value
                Relativity.RELATIVE_TO_FIRST -> first + value
                Relativity.RELATIVE_TO_SECOND -> second + value
            }
        }

        private fun parseSingle(string: String): Pair<Double, Relativity> {
            return if (string.startsWith("~~")) {
                Pair(string.substring(2).toZeroWhenEmpty().toDouble(), Relativity.RELATIVE_TO_SECOND)
            } else if (string.startsWith("~")) {
                Pair(string.substring(1).toZeroWhenEmpty().toDouble(), Relativity.RELATIVE_TO_FIRST)
            } else {
                Pair(string.toDouble(), Relativity.ABSOLUTE)
            }
        }
    }
}
