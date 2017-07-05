package com.ysered.circleprogress

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.ysered.circleprogress.view.CircleProgressView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val progressBar = findViewById(R.id.progressBar) as CircleProgressView
        val progressSeekBar = findViewById(R.id.seekBar) as SeekBar
        progressBar.setProgress(progressSeekBar.progress)

        progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                progressBar.setProgress(progress)
                if (progress < 40) {
                    progressBar.setText("Good")
                } else if (progress in 40..70) {
                    progressBar.setText("Normal")
                } else {
                    progressBar.setText("Bad")
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }
}
