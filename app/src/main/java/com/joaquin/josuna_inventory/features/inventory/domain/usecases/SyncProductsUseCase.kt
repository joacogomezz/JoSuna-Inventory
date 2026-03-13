package com.joaquin.josuna_inventory.features.inventory.domain.usecases

import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class SyncProductsUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke() = repository.syncProducts()
}

