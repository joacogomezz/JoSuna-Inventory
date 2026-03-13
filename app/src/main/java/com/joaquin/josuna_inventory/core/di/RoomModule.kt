package com.joaquin.josuna_inventory.core.di

import android.content.Context
import androidx.room.Room
import com.joaquin.josuna_inventory.core.database.SmartStockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SmartStockDatabase {
        return Room.databaseBuilder(
            context,
            SmartStockDatabase::class.java,
            "josuna_inventory_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideProductDao(database: SmartStockDatabase) = database.productDao()
}

