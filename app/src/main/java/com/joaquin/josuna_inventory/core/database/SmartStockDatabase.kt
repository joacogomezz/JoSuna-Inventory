package com.joaquin.josuna_inventory.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.joaquin.josuna_inventory.features.inventory.data.datasources.local.dao.ProductDao
import com.joaquin.josuna_inventory.features.inventory.data.datasources.local.entity.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = true
)
abstract class SmartStockDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}

