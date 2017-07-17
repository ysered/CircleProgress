package com.ysered.circleprogress

import android.content.Context
import android.os.Build
import android.util.Log

fun Any.debug(text: String) {
    Log.d(this::class.java.simpleName, text)
}

fun Context.getResolvedColor(color: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(color)
    } else {
        resources.getColor(color)
    }
}
