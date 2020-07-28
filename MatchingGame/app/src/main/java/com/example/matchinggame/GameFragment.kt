package com.example.matchinggame

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.matchinggame.model.SequenceGenerator.Companion.ButtonColors as ButtonColors

class GameFragment(private val player: SequencePlayer) :
    Fragment(),
    SequenceListener,
    View.OnTouchListener {
    // Keeps track of button mode (sequence play vs. user input) and presses
    override var userInputMode = false
    override val presses = ArrayList<ButtonColors>(SequencePlayer.LENGTH_END)
    override var readyToCheck = false

    // Game stats
    override var level: Int = 1
    override var sequence: Array<ButtonColors>? = null

    // Tones to play with buttons
    override lateinit var orangeNote: MediaPlayer
    override lateinit var greenNote: MediaPlayer
    override lateinit var blueNote: MediaPlayer
    override lateinit var yellowNote: MediaPlayer

    // Game buttons
    override lateinit var btn_orange: ImageView
    override lateinit var btn_green: ImageView
    override lateinit var btn_blue: ImageView
    override lateinit var btn_yellow: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orangeNote = MediaPlayer.create(requireContext(), R.raw.c)
        greenNote = MediaPlayer.create(requireContext(), R.raw.e)
        blueNote = MediaPlayer.create(requireContext(), R.raw.g)
        yellowNote = MediaPlayer.create(requireContext(), R.raw.a)

        orangeNote.isLooping = true
        greenNote.isLooping = true
        blueNote.isLooping = true
        yellowNote.isLooping = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        btn_blue = view.findViewById(R.id.btn_blue)
        btn_green = view.findViewById(R.id.btn_green)
        btn_orange = view.findViewById(R.id.btn_orange)
        btn_yellow = view.findViewById(R.id.btn_yellow)

        setListeners(btn_orange, ButtonColors.ORANGE)
        setListeners(btn_green, ButtonColors.GREEN)
        setListeners(btn_blue, ButtonColors.BLUE)
        setListeners(btn_yellow, ButtonColors.YELLOW)

        if (container?.id == R.id.first_container_id) {
            player.initP1Frag(this)
            // Give only player 1 the control to start the game
            view.findViewById<Button>(R.id.start_button).setOnClickListener {
                sequence = player.playSequence()
            }
        } else {
            player.initP2Frag(this)
            // Remove start button from player 2's screen
            view.findViewById<Button>(R.id.start_button).visibility = View.GONE
        }

        return view
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view !is ImageView)
            return false

        when (view) {
            btn_blue -> {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_blue_lit, null)
                        )
                        blueNote.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_blue_unlit, null)
                        )
                        blueNote.pause()
                    }
                }
            }
            btn_green -> {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_green_lit, null)
                        )
                        greenNote.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_green_unlit, null)
                        )
                        greenNote.pause()
                    }
                }
            }
            btn_orange -> {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_orange_lit, null)
                        )
                        orangeNote.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_orange_unlit, null)
                        )
                        orangeNote.pause()
                    }
                }
            }
            btn_yellow -> {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_yellow_lit, null)
                        )
                        yellowNote.start()
                    }
                    MotionEvent.ACTION_UP -> {
                        view.setImageDrawable(
                            resources.getDrawable(R.drawable.tile_yellow_unlit, null)
                        )
                        yellowNote.pause()
                    }
                }
            }
        }

        if (motionEvent.action == MotionEvent.ACTION_UP && userInputMode)
            view.performClick()

        return true
    }

    private fun setListeners(btn: ImageView, buttonColor: ButtonColors) {
        btn.setOnTouchListener(this)
        btn.setOnClickListener {
            Log.d(this::class.java.toString(), "Button pressed: $buttonColor")
            if (userInputMode) {
                presses.add(buttonColor)

                if (presses.size == sequence?.size) {
                    userInputMode = false
                    readyToCheck = true
                    player.checkUserInput()
                }
            }
        }
    }

    override fun increaseLevel() {
        level++
        readyToCheck = false
        presses.clear()
        view?.findViewById<Button>(R.id.start_button)?.text =
            resources.getString(R.string.start_level, level)
    }

    override fun displayEndScreen(resId: Int, finished: Boolean) {
        val message = if (finished) resources.getString(resId)
        else "${resources.getString(resId)} $level"
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun checkUserInput(): Boolean {
        var passed = true

        for (i in 0 until presses.size) {
            passed = passed && presses[i] == sequence!![i]
        }

        return passed
    }
}
