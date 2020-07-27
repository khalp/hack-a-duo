package com.example.matchinggame.model

import java.lang.IllegalArgumentException
import java.util.Random

class SequenceGenerator {
    companion object {
        enum class ButtonColors { RED, GREEN, BLUE, YELLOW }

        private const val NUM_BUTTONS = 4

        /***
         * Generate a sequence of random button values
         *
         * @precondition: sequence length must be greater than or equal to 1
         * @param length: length of sequence to generate
         */
        fun generateSequence(length: Int): Array<ButtonColors> {
            if (length < 1)
                throw IllegalArgumentException("Length of sequence $length must be >= 1")

            val output = Array(length) { _ -> ButtonColors.RED }
            val random = Random()

            for (i in 0 until length) {
                output[i] = intToButtons(random.nextInt(NUM_BUTTONS))
            }

            return output
        }

        /**
         * Converts integer value to button value
         *
         * @precondition int value must be between 0 and 3 inclusive because
         * there are only four buttons
         * @param i: int value to convert to button value
         */
        fun intToButtons(i: Int): ButtonColors {
            return when (i) {
                0 -> ButtonColors.RED
                1 -> ButtonColors.GREEN
                2 -> ButtonColors.BLUE
                3 -> ButtonColors.YELLOW
                else -> throw IllegalArgumentException("Int value $i must be in range [0, 3] to convert to button value")
            }
        }
    }
}
