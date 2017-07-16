package com.ysered.circleprogress

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.ysered.circleprogress.view.CircleProgressView

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val progressBar = findViewById(R.id.progressBar) as CircleProgressView
        progressBar.progressText = "Off"

        val mySeekBar = findViewById(R.id.seekBar) as SeekBar
        mySeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val (color, text) = when (progress) {
                    in 0..40 -> Color.GREEN to "Good"
                    in 40..70 -> Color.YELLOW to "Normal"
                    else -> Color.RED to "Bad"
                }
                progressBar.apply {
                    this.progress = progress
                    progressColor = color
                    progressTextColor = color
                    progressText = text
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}