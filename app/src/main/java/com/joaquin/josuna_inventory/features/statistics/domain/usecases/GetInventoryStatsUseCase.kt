package com.joaquin.josuna_inventory.features.statistics.domain.usecases

import com.joaquin.josuna_inventory.features.statistics.domain.entities.InventoryStats
import com.joaquin.josuna_inventory.features.statistics.domain.repositories.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInventoryStatsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {
    operator fun invoke(): Flow<InventoryStats> = repository.getStats()
}

