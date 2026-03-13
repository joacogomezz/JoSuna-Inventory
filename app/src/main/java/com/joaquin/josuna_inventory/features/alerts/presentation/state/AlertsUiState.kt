package com.joaquin.josuna_inventory.features.alerts.presentation.state

import com.joaquin.josuna_inventory.features.alerts.domain.entities.LowStockAlert

data class AlertsUiState(
    val alerts: List<LowStockAlert> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val alertCount: Int get() = alerts.size
    val criticalCount: Int get() = alerts.count { it.isCritical }
}

