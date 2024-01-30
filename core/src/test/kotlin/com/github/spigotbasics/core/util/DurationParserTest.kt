package com.github.spigotbasics.core.util

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class DurationParserTest {
    @Test
    fun parseDurationToTicks() {
        assertEquals(69, DurationParser.parseDurationToTicks("69t"))

        assertEquals(42 * 20, DurationParser.parseDurationToTicks("42"))
        assertEquals(42 * 20, DurationParser.parseDurationToTicks("42s"))
        assertEquals(42 * 20, DurationParser.parseDurationToTicks("42 sec"))

        assertEquals(42 * 60 * 20, DurationParser.parseDurationToTicks("42m"))
        assertEquals(42 * 60 * 20 + 1 * 20, DurationParser.parseDurationToTicks("42 min 1sec"))

        assertEquals(10, DurationParser.parseDurationToTicks("0.5"))
        assertEquals(10, DurationParser.parseDurationToTicks("0.5s"))

        assertEquals(0, DurationParser.parseDurationToTicks("49ms"))
        assertEquals(1, DurationParser.parseDurationToTicks("50ms"))

        assertThrows<IllegalArgumentException> { DurationParser.parseDurationToTicks("42 asd") }
    }

    @Test
    fun parseDurationToMillis() {
        assertEquals(69000, DurationParser.parseDurationToMillis("69"))
        assertEquals(69000, DurationParser.parseDurationToMillis("69s"))
        assertEquals(69, DurationParser.parseDurationToMillis("69 ms"))
    }
}