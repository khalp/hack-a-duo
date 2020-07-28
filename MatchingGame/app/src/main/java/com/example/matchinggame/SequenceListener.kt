package com.example.matchinggame

import android.media.MediaPlayer
import android.widget.ImageView
import com.example.matchinggame.model.SequenceGenerator.Companion.ButtonColors as ButtonColors

interface SequenceListener {
    // Keeps track of button mode (sequence play vs. user input) and presses
    var userInputMode: Boolean
    val presses: ArrayList<ButtonColors>
    var readyToCheck: Boolean

    // Game stats
    var level: Int
    var sequence: Array<ButtonColors>?

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

    fun disableButtons() {
        btn_orange.isClickable = false
        btn_green.isClickable = false
        btn_blue.isClickable = false
        btn_yellow.isClickable = false

        userInputMode = false
    }

    fun enableButtons() {
        btn_orange.isClickable = true
        btn_green.isClickable = true
        btn_blue.isClickable = true
        btn_yellow.isClickable = true

        userInputMode = true
    }

    fun checkUserInput(): Boolean
    fun displayEndScreen(resId: Int, finished: Boolean)
    fun increaseLevel()
}
