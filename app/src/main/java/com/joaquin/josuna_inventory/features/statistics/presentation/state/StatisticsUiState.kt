package com.joaquin.josuna_inventory.features.statistics.presentation.state

import com.joaquin.josuna_inventory.features.statistics.domain.entities.InventoryStats

data class StatisticsUiState(
    val stats: InventoryStats = InventoryStats(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
