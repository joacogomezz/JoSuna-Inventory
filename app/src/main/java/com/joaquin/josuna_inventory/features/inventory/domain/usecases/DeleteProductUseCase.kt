package com.joaquin.josuna_inventory.features.inventory.domain.usecases

import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(id: String) = repository.deleteProduct(id)
}

