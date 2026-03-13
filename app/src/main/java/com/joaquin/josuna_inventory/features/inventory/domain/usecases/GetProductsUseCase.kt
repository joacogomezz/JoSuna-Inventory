package com.joaquin.josuna_inventory.features.inventory.domain.usecases

import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    operator fun invoke(): Flow<List<Product>> = repository.getProducts()
}

