package com.github.spigotbasics.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DictionaryTest {
    @Test
    fun isCaseInsensitive() {
        val map = Dictionary<String>()
        map["test"] = "test"
        assertEquals("test", map["test"])
        assertEquals("test", map["TEST"])
        assertEquals("test", map["TesT"])

        map["TEST"] = "test2"
        assertEquals("test2", map["test"])
        assertEquals("test2", map["TEST"])
        assertEquals("test2", map["TesT"])

        map["TEST2"] = "TEST2"
        assertEquals("TEST2", map["test2"])
        assertEquals("TEST2", map["TEST2"])
        assertEquals("TEST2", map.remove("test2"))
        assertNull(map["test2"])
        assertNull(map["TEST2"])
    }

}