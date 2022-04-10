package com.example.accapp

import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Toast
import java.lang.Math.abs


class MainActivity : AppCompatActivity() {
    val handler = Handler()
    var flag = false
    val interval: Long = 1000
    var isUp = false

    val processSensors: Runnable = object : Runnable {
        override fun run() {
            // Do work with the sensor values.
            flag = true
            // The Runnable is posted to run again here:
            handler.postDelayed(this, interval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sensorManager = (getSystemService(Context.SENSOR_SERVICE) as SensorManager)

        val acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val valuesAccel = FloatArray(3)
        val valuesAccelMotion = FloatArray(3)
        val valuesAccelGravity = FloatArray(3)
        val valuesLinAccel = FloatArray(3)
        val valuesGravity = FloatArray(3)

        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

                if (flag) {
                    for (i in 0..2) {
                        valuesAccel[i] = (event?.values?.get(i))!!
                        valuesAccelGravity[i] =
                            (0.1 * (event.values[i]) + 0.9 * valuesAccelGravity[i]).toFloat()
                        valuesAccelMotion[i] = (event.values[i]
                                - valuesAccelGravity[i])
                    }

                    if (Data.starY == 0f) {
                        Data.starY = valuesAccel[1]
                    }

                    if (valuesAccel[1] - Data.starY > 5) {
                        if (isUp.not()) {
                            isUp = true
                            Data.score++
                            Data.prevY = valuesAccel[1]
                        }
                    } else {
                        isUp = false
                    }

//                if (valuesAccel[1] > 5) {
//                    Data.score++
//                }

                    Log.d("CCC", (valuesAccel[1]).toString())
                    Log.d("CCC", (Data.prevY).toString())

                    Log.d("AAA", valuesAccel.toList().toString())
                    Log.d("AAA", valuesAccelGravity.toList().toString())
                    Log.d("AAA", valuesAccelMotion.toList().toString())/*
                Log.d("AAA", valuesGravity.toList().toString())
                Log.d("AAA", valuesLinAccel.toList().toString())*/

                    Log.d("BBB", Data.score.toString())
                    flag = false
                }
//                val gravity = p0?.values?.clone()?.get(0)
//
//                Toast.makeText(this@MainActivity, gravity.toString(), Toast.LENGTH_SHORT).show()

            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }, acc, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(processSensors)
    }

    override fun onResume() {
        super.onResume()
        handler.post(processSensors)
    }
}