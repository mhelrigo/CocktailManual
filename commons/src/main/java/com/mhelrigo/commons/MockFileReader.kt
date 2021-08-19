package com.mhelrigo.commons

import java.io.InputStreamReader

/**
 * Reads the mock results created @mhelrigo.cocktailmanual.data.resources
 * For test purposes only.
 * */
class MockFileReader(path: String) {
    val content: String

    init {
        val reader = InputStreamReader(this.javaClass.classLoader?.getResourceAsStream(path))
        content = reader.readText()
        reader.close()
    }
}