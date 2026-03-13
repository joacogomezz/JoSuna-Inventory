package com.joaquin.josuna_inventory.features.alerts.domain.repositories

import com.joaquin.josuna_inventory.features.alerts.domain.entities.LowStockAlert
import kotlinx.coroutines.flow.Flow

interface AlertsRepository {
    fun getLowStockAlerts(threshold: Int): Flow<List<LowStockAlert>>
}

