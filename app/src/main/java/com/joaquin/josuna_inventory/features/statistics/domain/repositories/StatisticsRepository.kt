package com.joaquin.josuna_inventory.features.statistics.domain.repositories

import com.joaquin.josuna_inventory.features.statistics.domain.entities.InventoryStats
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {
    fun getStats(): Flow<InventoryStats>
}

