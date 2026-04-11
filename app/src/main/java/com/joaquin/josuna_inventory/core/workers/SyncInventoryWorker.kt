package com.joaquin.josuna_inventory.core.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import com.joaquin.josuna_inventory.features.inventory.domain.usecases.SyncProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncInventoryWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncProductsUseCase: SyncProductsUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            syncProductsUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

