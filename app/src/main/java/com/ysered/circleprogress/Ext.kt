package com.ysered.circleprogress

import android.util.Log

fun Any.debug(text: String) {
    Log.d(this::class.java.simpleName, text)
}