package com.example.matchinggame

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class GameFragment(private val player: SequencePlayer) : Fragment(), SequenceListener, View.OnTouchListener {
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

        setOnClickListener(btn_orange, 0)
        setOnClickListener(btn_green, 1)
        setOnClickListener(btn_blue, 2)
        setOnClickListener(btn_yellow, 3)

        if (container?.id == R.id.first_container_id)
            player.initP1Frag(this)
        else
            player.initP2Frag(this)

        return view
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view is ImageView) {
            when (view) {
                btn_blue -> {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_blue_lit, null))
                        }
                        MotionEvent.ACTION_UP -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_blue_unlit, null))
                        }
                    }
                }
                btn_green -> {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_green_lit, null))
                        }
                        MotionEvent.ACTION_UP -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_green_unlit, null))
                        }
                    }
                }
                btn_orange -> {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_orange_lit, null))
                        }
                        MotionEvent.ACTION_UP -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_orange_unlit, null))
                        }
                    }
                }
                btn_yellow -> {
                    when (motionEvent.action) {
                        MotionEvent.ACTION_DOWN -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_yellow_lit, null))
                        }
                        MotionEvent.ACTION_UP -> {
                            view.setImageDrawable(resources.getDrawable(R.drawable.tile_yellow_unlit, null))
                        }
                    }
                }
            }
        }
        if (motionEvent.action == MotionEvent.ACTION_UP)
            view.performClick()

        return true
    }

    private fun setOnClickListener(btn: ImageView, value: Int) {
        btn.setOnTouchListener(this)

        btn.setOnClickListener {
            Log.d(this::class.java.toString(), "Button pressed: $value")
        }
    }

    override fun gameOver() {
        TODO("Not yet implemented")
    }

    override fun acceptUserInput() {
        TODO("Not yet implemented")
    }
}
