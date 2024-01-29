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

        assertThrows<IllegalArgumentException> { DurationParser.parseDurationToTicks("42 asd") }
    }
}