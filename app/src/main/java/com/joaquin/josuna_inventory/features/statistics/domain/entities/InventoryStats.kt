package com.joaquin.josuna_inventory.features.statistics.domain.entities

data class InventoryStats(
    val totalProducts: Int = 0,
    val totalUnits: Int = 0,
    val totalValue: Double = 0.0,
    val mostExpensiveProduct: String = "",
    val lowestStockProduct: String = "",
    val averagePrice: Double = 0.0
)

