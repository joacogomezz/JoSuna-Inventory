package com.joaquin.josuna_inventory.core.hardware.vibrator

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VibratorManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    // Al guardar producto — vibración corta suave
    fun vibrateOnSave() {
        vibrate(longArrayOf(0, 80), -1)
    }

    // Al editar producto — vibración media
    fun vibrateOnEdit() {
        vibrate(longArrayOf(0, 60, 60, 60), -1)
    }

    // Al eliminar producto — vibración fuerte larga
    fun vibrateOnDelete() {
        vibrate(longArrayOf(0, 200, 100, 200), -1)
    }

    private fun vibrate(pattern: LongArray, repeat: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val amplitudes = pattern.map {
                if (it == 0L) 0 else VibrationEffect.DEFAULT_AMPLITUDE
            }.toIntArray()
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, amplitudes, repeat))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, repeat)
        }
    }
}

