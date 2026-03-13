package com.joaquin.josuna_inventory.features.alerts.domain.entities

data class LowStockAlert(
    val productId: String,
    val productName: String,
    val currentStock: Int,
    val threshold: Int = 5
) {
    val isCritical: Boolean get() = currentStock == 0
}

