package com.ysered.circleprogress

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listOf(R.id.firstButton, R.id.secondButton)
                .forEach { findViewById(it).setOnClickListener(this@MainActivity) }
    }

    override fun onClick(view: View) {
        val activityClass = when (view.id) {
            R.id.firstButton -> FirstActivity::class.java
            R.id.secondButton -> TODO("Add handler for second button")
            else -> throw RuntimeException("OnClick for ${view.id} not implemented")
        }
        startActivity(Intent(this, activityClass))
    }
}
