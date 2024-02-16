package com.github.spigotbasics.core.util

object DurationParser {
    private val regex =
        (
                "(\\d+(?:\\.\\d+)?)\\s*" + // Number part
                        "(" +
                        "ms|milli|millis|millisecond|milliseconds|" +
                        "t|tick|ticks|" +
                        "s|sec|secs|second|seconds|" +
                        "m|min|mins|minute|minutes|" +
                        "h|hour|hours|" +
                        "d|day|days|" +
                        "w|week|weeks" +
                        ")?"
                ).toRegex(
                RegexOption.IGNORE_CASE,
            )
    private val onlyNumbers = """(\d+(?:\.\d+)?)""".toRegex(RegexOption.IGNORE_CASE)

    fun parseDurationToTicks(input: String): Long {
        return parseDurationToMillis(input) / 50
    }

    fun parseDurationToMillis(input: String): Long {
        if (onlyNumbers.matches(input)) {
            return (input.toDouble() * 1000).toLong()
        }

        var totalSeconds = 0.0

        regex.findAll(input).forEach { matchResult ->
            val (value, unit) = matchResult.destructured
            val numericValue = value.toDouble()

            totalSeconds +=
                when (unit.lowercase()) {
                    "ms", "milli", "millis", "millisecond", "milliseconds" -> numericValue
                    "t", "tick", "ticks" -> numericValue * 50
                    "s", "sec", "secs", "second", "seconds" -> numericValue * 1000
                    "m", "min", "mins", "minute", "minutes" -> numericValue * 60 * 1000
                    "h", "hour", "hours" -> numericValue * 3600 * 1000
                    "d", "day", "days" -> numericValue * 86400 * 1000
                    "w", "week", "weeks" -> numericValue * 604800 * 1000
                    else -> throw IllegalArgumentException("Invalid unit: $unit")
                }
        }

        return totalSeconds.toLong()
    }
}
