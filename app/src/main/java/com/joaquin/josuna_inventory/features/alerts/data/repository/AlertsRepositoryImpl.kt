package com.joaquin.josuna_inventory.features.alerts.data.repository

import com.joaquin.josuna_inventory.features.alerts.domain.entities.LowStockAlert
import com.joaquin.josuna_inventory.features.alerts.domain.repositories.AlertsRepository
import com.joaquin.josuna_inventory.features.inventory.data.datasources.local.dao.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AlertsRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : AlertsRepository {

    override fun getLowStockAlerts(threshold: Int): Flow<List<LowStockAlert>> {
        return productDao.getAllProducts().map { products ->
            products
                .filter { it.quantity <= threshold }
                .sortedBy { it.quantity }
                .map { product ->
                    LowStockAlert(
                        productId = product.id,
                        productName = product.name,
                        currentStock = product.quantity,
                        threshold = threshold
                    )
                }
        }
    }
}

