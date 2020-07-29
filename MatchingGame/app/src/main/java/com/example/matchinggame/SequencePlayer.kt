package com.example.matchinggame

import android.os.Handler
import android.util.Log
import com.example.matchinggame.model.SequenceGenerator
import kotlinx.coroutines.Runnable

class SequencePlayer {
    companion object {
        // Can change this in the future, but plan for 4 levels (sequence length increases by one every time)
        const val LENGTH_START = 2
        const val LENGTH_END = 5
    }

    private var sequenceLength: Int = LENGTH_START
    private lateinit var p1Frag: SequenceListener
    private var p2Frag: SequenceListener? = null

    fun initP1Frag(frag: SequenceListener) {
        p1Frag = frag
    }

    fun initP2Frag(frag: SequenceListener) {
        p2Frag = frag
    }

    fun playSequence() {
        try {
            if (!this::p1Frag.isInitialized)
                throw IllegalStateException("Cannot play sequence before initializing fragment")

            // Disable user input
            p1Frag.disableButtons()
            p2Frag?.disableButtons()

            // Update level display
            p1Frag.updateLevelDisplay(true)
            p2Frag?.updateLevelDisplay(true)

            // Play generated sequence
            val sequence = SequenceGenerator.generateSequence(sequenceLength)
            val noteLength = 700L
            val noteDelay = 150L

            var handler = Handler()
            for (i in sequence.indices) {
                handler.postDelayed(
                    {
                        if (i > 0) {
                            p1Frag.stopNote(sequence[i - 1])
                            p2Frag?.stopNote(sequence[i - 1])
                        }
                    },
                    i * noteLength + (i - 1) * noteDelay
                )
                handler.postDelayed(
                    Runnable {
                        p1Frag.playNote(sequence[i])
                        p2Frag?.playNote(sequence[i])
                    },
                    i * (noteLength + noteDelay)
                )
                handler = Handler()
            }
            handler.postDelayed(
                Runnable {
                    p1Frag.stopNote(sequence[sequence.lastIndex])
                    p2Frag?.stopNote(sequence[sequence.lastIndex])

                    // Enable user input
                    p1Frag.enableButtons()
                    p2Frag?.enableButtons()
                },
                sequence.size * noteLength + (sequence.size - 1) * noteDelay
            )

            p1Frag.sequence = sequence
            p2Frag?.sequence = sequence
        } catch (e: Exception) {
            Log.e(this.javaClass.toString(), e.message.toString())
        }
    }

    fun checkUserInput() {
        if (p1Frag.readyToCheck && p2Frag == null) {
            // Single player
            if (p1Frag.checkUserInput()) {
                increaseLevel(p1Frag)
            } else {
                gameOver(false)
            }
        } else if (p1Frag.readyToCheck && p2Frag != null && p2Frag!!.readyToCheck) {
            // Multiplayer
            val p1 = p1Frag.checkUserInput()
            val p2 = p2Frag!!.checkUserInput()

            if (p1 && p2) {
                increaseLevel(p1Frag, p2Frag)
            } else if (p1 && !p2) {
                gameOver(false, 1)
            } else if (!p1 && p2) {
                gameOver(false, 2)
            } else {
                gameOver(false, 0)
            }
        }
    }

    private fun increaseLevel(frag1: SequenceListener, frag2: SequenceListener? = null) {
        if (sequenceLength >= LENGTH_END) {
            gameOver(true)
        } else {
            sequenceLength++
            frag1.increaseLevel()
            frag2?.increaseLevel()
        }
    }

    private fun gameOver(finished: Boolean, winningSide: Int? = null) {
        if (finished) {
            // All levels completed
            if (winningSide == null) {
                // Single player
                p1Frag.displayEndScreen(R.string.won_solo, finished)
            } else {
                p1Frag.displayEndScreen(R.string.won_tie_multiplayer, finished)
            }
        } else {
            // Game finished before levels were completed
            if (winningSide == null) {
                // Single player
                p1Frag.displayEndScreen(R.string.lost_solo, finished)
            } else {
                // Multiplayer
                when (winningSide) {
                    0 -> {
                        // Both failed
                        p2Frag?.displayEndScreen(R.string.lost_tie_multiplayer, finished)
                    }
                    1 -> {
                        // Player one won
                        p1Frag.displayEndScreen(R.string.won_player_one, finished)
                    }
                    2 -> {
                        // Player two won
                        p2Frag?.displayEndScreen(R.string.won_player_two, finished)
                    }
                    else -> throw IllegalArgumentException("Losing side $winningSide must be in range [0, 2]")
                }
            }
        }

        restart()
    }

    fun restart() {
        p1Frag.restart()
        p2Frag?.restart()
        sequenceLength = LENGTH_START
    }
}
