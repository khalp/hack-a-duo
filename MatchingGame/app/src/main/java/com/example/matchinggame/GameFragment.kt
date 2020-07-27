package com.example.matchinggame

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.matchinggame.model.SequenceGenerator

class GameFragment(private val player: SequencePlayer) : Fragment(), SequenceListener {
    // Tones to play with buttons
    override lateinit var redNote: MediaPlayer
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

        redNote = MediaPlayer.create(requireContext(), R.raw.c)
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

        player.initP1Frag(this)

        return view
    }

    private fun setOnClickListener(btn: ImageView, value: Int) {
        btn.setOnClickListener {
            Log.d(this::class.java.toString(), "Button pressed: $value")

            // TODO: remove this, just using it to show that sequences/notes are playing
            player.playSequence()
        }
    }

    override fun gameOver() {
        TODO("Not yet implemented")
    }

    override fun acceptUserInput() {
        TODO("Not yet implemented")
    }
}
