package com.joaquin.josuna_inventory.features.alerts.domain.usecases

import com.joaquin.josuna_inventory.features.alerts.domain.entities.LowStockAlert
import com.joaquin.josuna_inventory.features.alerts.domain.repositories.AlertsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLowStockAlertsUseCase @Inject constructor(
    private val repository: AlertsRepository
) {
    operator fun invoke(threshold: Int = 5): Flow<List<LowStockAlert>> =
        repository.getLowStockAlerts(threshold)
}

