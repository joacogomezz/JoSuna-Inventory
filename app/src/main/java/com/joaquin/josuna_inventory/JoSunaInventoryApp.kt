package com.joaquin.josuna_inventory

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.joaquin.josuna_inventory.core.workers.SyncInventoryWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class JoSunaInventoryApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        scheduleInventorySync()
    }

    private fun scheduleInventorySync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // solo con internet
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncInventoryWorker>(
            15, TimeUnit.MINUTES // cada 15 min — mínimo permitido
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            SyncInventoryWorker.WORK_NAME,
            androidx.work.ExistingPeriodicWorkPolicy.KEEP, // si ya existe no la reemplaza
            syncRequest
        )
    }
}
