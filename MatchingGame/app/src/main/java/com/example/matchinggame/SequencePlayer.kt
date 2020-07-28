package com.example.matchinggame

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageView
import com.example.matchinggame.model.SequenceGenerator
import java.lang.IllegalStateException
import com.example.matchinggame.model.SequenceGenerator.Companion.ButtonColors as ButtonColors

class SequencePlayer(private val context: Context) {
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

            val sequence = SequenceGenerator.generateSequence(sequenceLength)

            for (bc in sequence) {
                p1Frag.playNote(bc)
                // p2Frag?.playNote(bc)
            }

//        p1Frag.acceptUserInput()
//        p2Frag?.acceptUserInput()
        } catch (e: Exception) {
            Log.e(this.javaClass.toString(), e.message.toString())
        }
    }

    fun increaseLevel() {
        if (sequenceLength >= LENGTH_END) {
            gameOver(true)
        } else {
            sequenceLength++
        }
    }

    // TODO: extract logic to individual listener fragments or interface
    fun gameOver(finished: Boolean, winningSide: Int? = null): String {
        return ""
//        // All levels completed
//        if (finished) {
//            return if (winningSide == null) {
//                // Single player
//                context.getString(R.string.won_solo)
//            } else {
//                context.getString(R.string.won_tie_multiplayer)
//            }
//        } else {
//            return if (winningSide == null) {
//                // Single player
//                context.getString(R.string.lost_solo)
//            } else {
//                // Multiplayer
//                when (winningSide) {
//                    0 -> {
//                        // Both failed
//                        context.getString(R.string.lost_tie_multiplayer)
//                    }
//                    1 -> {
//                        // Player one won
//                        context.getString(R.string.won_player_one)
//                    }
//                    2 -> {
//                        // Player two won
//                        context.getString(R.string.won_player_two)
//                    }
//                    else -> throw IllegalArgumentException("Losing side $winningSide must be in range [0, 2]")
//                }
//            }
//        }
    }
}

interface SequenceListener {
    // Tones to play with buttons
    var orangeNote: MediaPlayer
    var greenNote: MediaPlayer
    var blueNote: MediaPlayer
    var yellowNote: MediaPlayer

    // Game buttons
    var btn_orange: ImageView
    var btn_green: ImageView
    var btn_blue: ImageView
    var btn_yellow: ImageView

    fun playNote(buttonColor: ButtonColors) {
        lateinit var note: MediaPlayer

        // TODO: put in actual code for lighting up button
        when (buttonColor) {
            ButtonColors.ORANGE -> {
                note = orangeNote
            }
            ButtonColors.GREEN -> {
                note = greenNote
            }
            ButtonColors.BLUE -> {
                note = blueNote
            }
            ButtonColors.YELLOW -> {
                note = yellowNote
            }
        }

        note.start()
        // REVISIT: intended to pause all actions until note has finished playing, but may
        // not be necessary/efficient or cause infinite looping haha (maybe we need threads?)
        while (note.isPlaying) {
        }
    }

    fun gameOver()

    fun acceptUserInput()
}
