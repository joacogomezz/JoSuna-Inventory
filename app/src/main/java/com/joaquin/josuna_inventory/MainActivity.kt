package com.joaquin.josuna_inventory

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.messaging.FirebaseMessaging
import com.joaquin.josuna_inventory.core.hardware.camera.CameraManager
import com.joaquin.josuna_inventory.core.navigation.NavigationWrapper
import com.joaquin.josuna_inventory.core.notifications.NotificationHelper
import com.joaquin.josuna_inventory.core.theme.ThemeViewModel
import com.joaquin.josuna_inventory.ui.theme.JoSunaInventoryTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var cameraManager: CameraManager

    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pedir permiso de notificaciones
        NotificationHelper.requestNotificationPermission(this)

        // Token FCM para pruebas
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("FCM_TOKEN", "Token: ${task.result}")
            }
        }

        enableEdgeToEdge()
        setContent {
            val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle()

            JoSunaInventoryTheme(darkTheme = isDarkMode) {
                NavigationWrapper(
                    cameraManager = cameraManager,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
