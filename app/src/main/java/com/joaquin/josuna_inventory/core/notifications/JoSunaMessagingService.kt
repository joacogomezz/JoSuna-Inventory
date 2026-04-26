package com.joaquin.josuna_inventory.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.joaquin.josuna_inventory.MainActivity
import com.joaquin.josuna_inventory.R

class JoSunaMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "JoSuna Inventory"

        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: "Tienes productos con stock bajo"

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aquí podrías guardar el token en Firebase si lo necesitas
    }

    private fun showNotification(title: String, body: String) {
        val channelId = CHANNEL_ID
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal de notificación
        val channel = NotificationChannel(
            channelId,
            "Alertas de Inventario",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones de stock bajo y alertas del inventario"
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)

        // Intent para abrir la app al tocar la notificación
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher_round) // Usamos el launcher icon ya que el drawable dado no existe
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 200, 100, 200))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "josuna_inventory_channel"
        const val NOTIFICATION_ID = 1001
    }
}
