package com.ysered.circleprogress

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ysered.circleprogress.view.CircleProgressView

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val progress = findViewById(R.id.progressBar) as CircleProgressView
        progress.progressText = "Off"
    }
}