package com.joaquin.josuna_inventory.features.inventory.domain.usecases

import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(product: Product) = repository.addProduct(product)
}

