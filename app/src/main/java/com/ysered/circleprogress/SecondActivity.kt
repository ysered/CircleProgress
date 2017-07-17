package com.ysered.circleprogress

import android.animation.ArgbEvaluator
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

        val goodStart = getResolvedColor(R.color.goodStart)
        val goodEnd = getResolvedColor(R.color.goodEnd)
        val normalStart = getResolvedColor(R.color.normalStart)
        val normalEnd = getResolvedColor(R.color.normalEnd)
        val badStart = getResolvedColor(R.color.badStart)
        val badEnd = getResolvedColor(R.color.badEnd)
        val colorEvaluator = ArgbEvaluator()

        val mySeekBar = findViewById(R.id.seekBar) as SeekBar
        mySeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val (color, text) = when (progress) {
                    in 0..40 -> colorEvaluator.evaluate(progress / 40f, goodStart, goodEnd) to "Good"
                    in 40..70 -> colorEvaluator.evaluate(progress / 70f, normalStart, normalEnd) to "Normal"
                    else -> colorEvaluator.evaluate(progress / 100f, badStart, badEnd) to "Bad"
                }
                progressBar.apply {
                    this.progress = progress
                    progressColor = color as Int
                    progressText = text
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}