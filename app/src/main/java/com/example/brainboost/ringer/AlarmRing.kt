package com.example.brainboost.ringer

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri

class AlarmRing private constructor(context: Context) {

    private var ringtone: Ringtone? = null

    init {
        val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context.applicationContext, ringtoneUri)
    }

    companion object {
        @Volatile
        private var INSTANCE: AlarmRing? = null

        fun getInstance(context: Context): AlarmRing =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: AlarmRing(context.applicationContext).also { INSTANCE = it }
            }
    }

    fun playRingtone() {
        ringtone?.play()
    }

    fun stopRingtone() {
        ringtone?.stop()
    }

    fun isPlaying(): Boolean {
        return ringtone?.isPlaying ?: false
    }
}
