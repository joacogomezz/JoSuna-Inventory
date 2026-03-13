package com.joaquin.josuna_inventory.core.hardware.sound

import android.content.Context
import android.media.RingtoneManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SoundManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun playSaveSound() {
        playSound(RingtoneManager.TYPE_NOTIFICATION)
    }

    fun playDeleteSound() {
        playSound(RingtoneManager.TYPE_NOTIFICATION)
    }

    private fun playSound(type: Int) {
        try {
            val uri = RingtoneManager.getDefaultUri(type)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
