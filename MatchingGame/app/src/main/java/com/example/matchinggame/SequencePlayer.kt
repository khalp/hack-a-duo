package com.example.matchinggame

import android.content.Context
import android.media.MediaPlayer
import android.widget.Button
import com.example.matchinggame.model.SequenceGenerator
import java.lang.IllegalArgumentException
import com.example.matchinggame.model.SequenceGenerator.Companion.ButtonColors as ButtonColors

// REVISIT: a lot of parameters, may have to change later
class SequencePlayer(
    private val context: Context,
    private val red1: Button,
    private val green1: Button,
    private val blue1: Button,
    private val yellow1: Button,
    private val red2: Button? = null,
    private val green2: Button? = null,
    private val blue2: Button? = null,
    private val yellow2: Button? = null
) {
    companion object {
        // Can change this in the future, but plan for 4 levels (sequence length increases by one every time)
        const val LENGTH_START = 2
        const val LENGTH_END = 5
    }

    private var sequenceLength: Int = LENGTH_START
    private val redNote = MediaPlayer.create(context, R.raw.c)
    private val greenNote = MediaPlayer.create(context, R.raw.e)
    private val blueNote = MediaPlayer.create(context, R.raw.g)
    private val yellowNote = MediaPlayer.create(context, R.raw.a)

    fun playSequence() {
        val sequence = SequenceGenerator.generateSequence(sequenceLength)

        for (bc in sequence) {
            playNote(bc)
        }
    }

    private fun playNote(buttonColor: ButtonColors) {
        lateinit var note: MediaPlayer

        // TODO: put in actual code for lighting up button
        when (buttonColor) {
            ButtonColors.RED -> {
                note = redNote
//                red1.setBackgroundColor(R.color.lightRed)
//                red2?.setBackgroundColor(R.color.lightRed)
            }
            ButtonColors.GREEN -> {
                note = greenNote
//                green1.setBackgroundColor(R.color.lightGreen)
//                green2?.setBackgroundColor(R.color.lightGreen)
            }
            ButtonColors.BLUE -> {
                note = blueNote
//                blue1.setBackgroundColor(R.color.lightBlue)
//                blue2?.setBackgroundColor(R.color.lightBlue)
            }
            ButtonColors.YELLOW -> {
                note = yellowNote
//                yellow1.setBackgroundColor(R.color.lightYellow)
//                yellow2?.setBackgroundColor(R.color.lightYellow)
            }
        }

        note.start()
        // REVISIT: intended to pause all actions until note has finished playing, but may
        // not be necessary/efficient or cause infinite looping haha (maybe we need threads?)
        while (note.isPlaying) {
        }
    }

    fun increaseLevel() {
        if (sequenceLength >= LENGTH_END) {
            gameOver(true)
        } else {
            sequenceLength++
        }
    }

    fun gameOver(finished: Boolean, winningSide: Int? = null): String {
        // All levels completed
        if (finished) {
            return if (winningSide == null) {
                // Single player
                context.getString(R.string.won_solo)
            } else {
                context.getString(R.string.won_tie_multiplayer)
            }
        } else {
            return if (winningSide == null) {
                // Single player
                context.getString(R.string.lost_solo)
            } else {
                // Multiplayer
                when (winningSide) {
                    0 -> {
                        // Both failed
                        context.getString(R.string.lost_tie_multiplayer)
                    }
                    1 -> {
                        // Player one won
                        context.getString(R.string.won_player_one)
                    }
                    2 -> {
                        // Player two won
                        context.getString(R.string.won_player_two)
                    }
                    else -> throw IllegalArgumentException("Losing side $winningSide must be in range [0, 2]")
                }
            }
        }
    }
}
