package com.example.matchinggame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class GameFragment : Fragment() {
    lateinit var btn_blue: ImageView
    lateinit var btn_green: ImageView
    lateinit var btn_orange: ImageView
    lateinit var btn_yellow: ImageView

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

        return view
    }

    private fun setOnClickListener(btn: ImageView, value: Int) {
        btn.setOnClickListener {
            Log.d(this::class.java.toString(), "Button pressed: $value")
        }
    }
}
