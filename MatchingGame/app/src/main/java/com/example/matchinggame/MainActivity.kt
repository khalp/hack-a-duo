package com.example.matchinggame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.device.dualscreen.core.ScreenHelper

class MainActivity : AppCompatActivity() {
    companion object {
        const val GAME = "game"
        const val MULTIPLAYER = "multi"
    }

    private lateinit var player: SequencePlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player = SequencePlayer()

        if (!ScreenHelper.isDualMode(this)) {
            // Remove the dual screen container fragments if they exist
            removeFragment(R.id.first_container_id)
            removeFragment(R.id.second_container_id)

            startGameFragment(R.id.first_container_id)
        } else {
            removeFragment(R.id.first_container_id)
            removeFragment(R.id.second_container_id)

            startGameFragment(R.id.first_container_id)
            startGameFragment(R.id.second_container_id)
        }
    }

    private fun removeFragment(containerId: Int) {
        supportFragmentManager.findFragmentById(containerId)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    private fun startGameFragment(container: Int) {
        supportFragmentManager.beginTransaction()
            .replace(container, GameFragment.newInstance(player), GAME)
            .commit()
    }
}
