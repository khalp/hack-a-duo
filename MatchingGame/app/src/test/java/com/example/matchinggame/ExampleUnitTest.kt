package com.example.matchinggame

import com.example.matchinggame.model.SequenceGenerator
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun sequenceGeneration() {
        // Test sequence generator through console output (and insure no errors are thrown)
        SequenceGenerator.generateSequence(5)
        println()
        SequenceGenerator.generateSequence(2)
        println()
    }
}
