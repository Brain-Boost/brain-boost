package com.example.opennewactivitybutton.ringer

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

class VibratePhone(private val context: Context) {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun vibrate() {
        if (vibrator.hasVibrator()) {
            val vibrationEffect = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            } else {
                // Deprecated in API 26
                @Suppress("DEPRECATION")
                return vibrator.vibrate(500)
            }
            vibrator.vibrate(vibrationEffect)
        }
    }
}
