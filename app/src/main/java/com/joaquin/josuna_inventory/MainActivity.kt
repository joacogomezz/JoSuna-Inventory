package com.joaquin.josuna_inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.joaquin.josuna_inventory.core.navigation.NavigationWrapper
import com.joaquin.josuna_inventory.core.hardware.camera.CameraManager
import com.joaquin.josuna_inventory.ui.theme.JoSunaInventoryTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var cameraManager: CameraManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoSunaInventoryTheme {
                NavigationWrapper(cameraManager = cameraManager)
            }
        }
    }
}
