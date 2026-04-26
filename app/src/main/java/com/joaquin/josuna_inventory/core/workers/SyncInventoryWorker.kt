package com.joaquin.josuna_inventory.core.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.joaquin.josuna_inventory.MainActivity
import com.joaquin.josuna_inventory.R
import com.joaquin.josuna_inventory.features.inventory.domain.repositories.InventoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncInventoryWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val inventoryRepository: InventoryRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Sincroniza Firebase → Room
            inventoryRepository.syncProducts()

            // Revisa stock bajo después de sincronizar
            checkLowStock()

            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
    }

    private suspend fun checkLowStock() {
        val products = inventoryRepository.getLocalProducts()
        val lowStock = products.filter { it.quantity <= 5 }

        if (lowStock.isNotEmpty()) {
            showLowStockNotification(lowStock.size)
        }
    }

    private fun showLowStockNotification(count: Int) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Alertas de Inventario",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("⚠️ Stock bajo detectado")
            .setContentText("$count producto(s) con stock bajo en tu inventario")
            .setSmallIcon(R.mipmap.ic_launcher_round) // Cambiado a ic_launcher_round para evitar error de compilación
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 200, 100, 200))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val WORK_NAME = "sync_inventory_worker"
        const val CHANNEL_ID = "josuna_inventory_channel"
        const val NOTIFICATION_ID = 1001
    }
}
