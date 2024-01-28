package com.github.spigotbasics.core.util

object DurationParser {

    private val regex = """(\d+(?:\.\d+)?)\s*(t|tick|ticks|s|sec|secs|second|seconds|m|min|mins|minute|minutes|h|hour|hours|d|day|days|w|week|weeks)?""".toRegex(RegexOption.IGNORE_CASE)
    private val onlyNumbers = """(\d+(?:\.\d+)?)""".toRegex(RegexOption.IGNORE_CASE)

    fun parseDurationToTicks(input: String): Long {

        if(onlyNumbers.matches(input)) {
            return (input.toDouble() * 20).toLong()
        }

        var totalSeconds = 0.0

        regex.findAll(input).forEach { matchResult ->
            val (value, unit) = matchResult.destructured
            val numericValue = value.toDouble()

            totalSeconds += when (unit.lowercase()) {
                "t", "tick", "ticks" -> numericValue
                "s", "sec", "secs", "second", "seconds" -> numericValue * 20
                "m", "min", "mins", "minute", "minutes" -> numericValue * 60 * 20
                "h", "hour", "hours" -> numericValue * 3600 * 20
                "d", "day", "days" -> numericValue * 86400 * 20
                "w", "week", "weeks" -> numericValue * 604800 * 20
                else -> throw IllegalArgumentException("Invalid unit: $unit")
            }
        }

        return totalSeconds.toLong()
    }

}