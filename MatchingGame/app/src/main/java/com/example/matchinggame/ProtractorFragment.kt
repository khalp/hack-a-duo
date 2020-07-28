package com.example.matchinggame

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment


class ProtractorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_protractor, container, false)

        setupSensors()

        return view
    }

    companion object {
        private const val HINGE_ANGLE_SENSOR_NAME = "Hinge Angle"
    }

    private lateinit var sensorManager: SensorManager
    private var mHingeAngleSensor: Sensor? = null
    private var mSensorListener: SensorEventListener? = null


    private fun setupSensors() {
        sensorManager = getSystemService<SensorManager>(
            requireContext(),
            SensorManager::class.java
        ) as SensorManager
        val sensorList: List<Sensor> =
            sensorManager.getSensorList(Sensor.TYPE_ALL)
        for (sensor in sensorList) {
            if (sensor.name.contains(HINGE_ANGLE_SENSOR_NAME)) {
                mHingeAngleSensor = sensor
            }
        }
        mSensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor == mHingeAngleSensor) {
                    view?.findViewById<TextView>(R.id.angle)?.text =
                        resources.getString(R.string.angle, event.values[0], "\u00B0")
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                //TODO
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (mHingeAngleSensor != null) {
            sensorManager!!.unregisterListener(mSensorListener, mHingeAngleSensor)
        }
    }

    override fun onResume() {
        super.onResume()
        if (mHingeAngleSensor != null) {
            sensorManager!!.registerListener(
                mSensorListener,
                mHingeAngleSensor,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
}