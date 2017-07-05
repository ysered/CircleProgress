package com.ysered.circleprogress

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import com.ysered.circleprogress.view.CircleProgressViewKt

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById(R.id.progressBar) as CircleProgressViewKt
        val progressSeekBar = findViewById(R.id.seekBar) as SeekBar
        progressBar.progress = progressSeekBar.progress

        progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.progress = progress
                progressBar.progressText = when (progress) {
                    in 0..40 -> "Good"
                    in 40..70 -> "Normal"
                    else -> "Bad"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
