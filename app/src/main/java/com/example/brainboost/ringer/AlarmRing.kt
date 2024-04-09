package com.example.brainboost.ringer

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

class AlarmRing(private val context: Context) {

    private var ringtone: Ringtone? = null

    init {
        val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
    }

    fun playRingtone() {
        ringtone?.play()
    }

    fun stopRingtone() {
        ringtone?.stop()
    }

    // If you need to check whether the ringtone is currently playing
    fun isPlaying(): Boolean {
        return ringtone?.isPlaying ?: false
    }
}
