package com.example.matchinggame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.microsoft.device.dualscreen.core.ScreenHelper

class MainActivity : AppCompatActivity() {
    companion object {
        const val GAME = "game"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!ScreenHelper.isDualMode(this)) {
            // Remove the dual screen container fragments if they exist
            removeFragment(R.id.first_container_id)
            removeFragment(R.id.second_container_id)

            startGameFragment(R.id.first_container_id)
        } else {
            // TODO: add dual screen action here
        }
    }

    private fun removeFragment(containerId: Int) {
        supportFragmentManager.findFragmentById(containerId)?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }
    }

    private fun startGameFragment(container: Int) {
        supportFragmentManager.beginTransaction()
            .replace(container, GameFragment(), GAME)
            .commit()
    }
}
