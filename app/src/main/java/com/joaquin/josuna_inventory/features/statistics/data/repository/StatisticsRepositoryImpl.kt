package com.joaquin.josuna_inventory.features.statistics.data.repository

import com.joaquin.josuna_inventory.features.inventory.data.datasources.local.dao.ProductDao
import com.joaquin.josuna_inventory.features.statistics.domain.entities.InventoryStats
import com.joaquin.josuna_inventory.features.statistics.domain.repositories.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : StatisticsRepository {

    override fun getStats(): Flow<InventoryStats> {
        return productDao.getAllProducts().map { products ->
            if (products.isEmpty()) return@map InventoryStats()

            val totalProducts = products.size
            val totalUnits = products.sumOf { it.quantity }
            val totalValue = products.sumOf { it.price * it.quantity }
            val averagePrice = products.map { it.price }.average()
            val mostExpensive = products.maxByOrNull { it.price }?.name ?: ""
            val lowestStock = products.minByOrNull { it.quantity }?.name ?: ""

            InventoryStats(
                totalProducts = totalProducts,
                totalUnits = totalUnits,
                totalValue = totalValue,
                mostExpensiveProduct = mostExpensive,
                lowestStockProduct = lowestStock,
                averagePrice = averagePrice
            )
        }
    }
}

