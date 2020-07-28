package com.example.matchinggame

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.matchinggame.model.SequenceGenerator.Companion.ButtonColors as ButtonColors

class GameFragment :
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

    companion object {
        lateinit var player: SequencePlayer
        internal fun newInstance(p: SequencePlayer) = GameFragment().apply {
            player = p
        }
    }

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
        val view: View = inflater.inflate(R.layout.fragment_game, container, false)

        // Set up game buttons
        btn_blue = view.findViewById(R.id.btn_blue)
        btn_green = view.findViewById(R.id.btn_green)
        btn_orange = view.findViewById(R.id.btn_orange)
        btn_yellow = view.findViewById(R.id.btn_yellow)

        setListeners(btn_orange, ButtonColors.ORANGE)
        setListeners(btn_green, ButtonColors.GREEN)
        setListeners(btn_blue, ButtonColors.BLUE)
        setListeners(btn_yellow, ButtonColors.YELLOW)

        // Initialize correct sequence player field with fragment
        if (container?.id == R.id.first_container_id) {
            player.initP1Frag(this)
        } else {
            player.initP2Frag(this)
        }

        // Set up start/restart game buttons
        view.findViewById<Button>(R.id.start_button).setOnClickListener {
            player.playSequence()
        }
        view.findViewById<Button>(R.id.restart_button).setOnClickListener {
            player.restart()
        }

        return view
    }

    override fun resetControlsAppearance() {
        view?.findViewById<Button>(R.id.start_button)?.let {
            it.text = resources.getString(R.string.start_game)
            it.visibility = View.VISIBLE
        }
        view?.findViewById<TextView>(R.id.level)?.let {
            it.text = resources.getString(R.string.level, level)
            it.visibility = View.INVISIBLE
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (view !is ImageView)
            return false

        when (view) {
            btn_blue -> processTouchEvent(motionEvent.action, view, blueNote)
            btn_green -> processTouchEvent(motionEvent.action, view, greenNote)
            btn_orange -> processTouchEvent(motionEvent.action, view, orangeNote)
            btn_yellow -> processTouchEvent(motionEvent.action, view, yellowNote)
        }

        if (motionEvent.action == MotionEvent.ACTION_UP && userInputMode)
            view.performClick()

        return true
    }

    private fun processTouchEvent(action: Int, button: ImageView, note: MediaPlayer) {
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (userInputMode) {
                    button.setImageDrawable(getLitTile(button))
                    note.start()
                }
            }
            MotionEvent.ACTION_POINTER_INDEX_MASK -> {
                button.setImageDrawable(getLitTile(button))
                note.start()
            }
            MotionEvent.ACTION_UP -> {
                if (userInputMode) {
                    button.setImageDrawable(getUnlitTile(button))
                    note.pause()
                }
            }
            MotionEvent.ACTION_POINTER_INDEX_SHIFT -> {
                button.setImageDrawable(getUnlitTile(button))
                note.pause()
            }
        }
    }

    private fun getLitTile(button: ImageView): Drawable? {
        return when (button) {
            btn_orange -> resources.getDrawable(R.drawable.tile_orange_lit, null)
            btn_green -> resources.getDrawable(R.drawable.tile_green_lit, null)
            btn_blue -> resources.getDrawable(R.drawable.tile_blue_lit, null)
            btn_yellow -> resources.getDrawable(R.drawable.tile_yellow_lit, null)
            else -> null
        }
    }

    private fun getUnlitTile(button: ImageView): Drawable? {
        return when (button) {
            btn_orange -> resources.getDrawable(R.drawable.tile_orange_unlit, null)
            btn_green -> resources.getDrawable(R.drawable.tile_green_unlit, null)
            btn_blue -> resources.getDrawable(R.drawable.tile_blue_unlit, null)
            btn_yellow -> resources.getDrawable(R.drawable.tile_yellow_unlit, null)
            else -> null
        }
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
        updateLevelDisplay(false)
    }

    override fun displayEndScreen(resId: Int, finished: Boolean) {
        val message = if (finished) resources.getString(resId)
        else "${resources.getString(resId)} $level"

        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message).setCancelable(false)
            .setPositiveButton(resources.getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }

        if (finished)
            builder.setPositiveButton(resources.getString(R.string.surprise)) { dialog, _ ->
                dialog.dismiss()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.first_container_id, ProtractorFragment(), null)
                    .commit()
            }.setCancelable(true)

        val dialog = builder.create()
        dialog.setTitle(resources.getString(R.string.game_over))
        dialog.show()
    }

    override fun checkUserInput(): Boolean {
        var passed = true

        for (i in 0 until presses.size) {
            passed = passed && presses[i] == sequence!![i]
        }

        return passed
    }

    override fun updateLevelDisplay(hideButton: Boolean) {
        if (hideButton) {
            view?.findViewById<TextView>(R.id.level)?.let {
                it.text = resources.getString(R.string.level, level)
                it.visibility = View.VISIBLE
            }

            view?.findViewById<Button>(R.id.start_button)?.visibility = View.INVISIBLE
        } else {
            view?.findViewById<TextView>(R.id.level)?.visibility = View.INVISIBLE

            view?.findViewById<Button>(R.id.start_button)?.let {
                it.text = resources.getString(R.string.start_level, level)
                it.visibility = View.VISIBLE
            }
        }
    }
}
