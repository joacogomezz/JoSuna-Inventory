package com.joaquin.josuna_inventory.core.di

import android.content.Context
import com.joaquin.josuna_inventory.core.hardware.camera.CameraManager
import com.joaquin.josuna_inventory.core.hardware.sound.SoundManager
import com.joaquin.josuna_inventory.core.hardware.vibrator.VibratorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HardwareModule {

    @Provides
    @Singleton
    fun provideVibratorManager(@ApplicationContext context: Context): VibratorManager {
        return VibratorManager(context)
    }

    @Provides
    @Singleton
    fun provideSoundManager(@ApplicationContext context: Context): SoundManager {
        return SoundManager(context)
    }

    @Provides
    @Singleton
    fun provideCameraManager(@ApplicationContext context: Context): CameraManager {
        return CameraManager(context)
    }
}

