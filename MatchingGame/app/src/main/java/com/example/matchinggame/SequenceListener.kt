package com.example.matchinggame

import android.media.MediaPlayer
import android.view.MotionEvent
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

    fun playNote(buttonColor: ButtonColors, start: Boolean = true) {
        if (start)
            triggerButton(MotionEvent.ACTION_DOWN, buttonColor)
    }

    fun stopNote(buttonColor: ButtonColors) {
        triggerButton(MotionEvent.ACTION_UP, buttonColor)
    }

    fun disableButtons() {
        userInputMode = false
    }

    fun enableButtons() {
        userInputMode = true
    }

    fun triggerButton(motionEvent: Int, buttonColor: ButtonColors) {
        if (!userInputMode) {
            val event = MotionEvent.obtain(0, 0, motionEvent, 0f, 0f, 0)
            when (buttonColor) {
                ButtonColors.ORANGE ->
                    btn_orange.dispatchTouchEvent(event)

                ButtonColors.GREEN ->
                    btn_green.dispatchTouchEvent(event)

                ButtonColors.BLUE ->
                    btn_blue.dispatchTouchEvent(event)

                ButtonColors.YELLOW ->
                    btn_yellow.dispatchTouchEvent(event)
            }
            event.recycle()
        }
    }

    fun checkUserInput(): Boolean
    fun displayEndScreen(resId: Int, finished: Boolean)
    fun increaseLevel()
}
