package com.joaquin.josuna_inventory.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.joaquin.josuna_inventory.features.auth.presentation.screens.LoginScreen
import com.joaquin.josuna_inventory.features.auth.presentation.screens.RegisterScreen
import com.joaquin.josuna_inventory.features.inventory.presentation.screens.AddProductScreen
import com.joaquin.josuna_inventory.features.inventory.presentation.screens.CameraScreen
import com.joaquin.josuna_inventory.features.inventory.presentation.screens.EditProductScreen
import com.joaquin.josuna_inventory.features.inventory.presentation.screens.InventoryHomeScreen
import com.joaquin.josuna_inventory.core.hardware.camera.CameraManager
import com.joaquin.josuna_inventory.features.alerts.presentation.screens.AlertsScreen
import com.joaquin.josuna_inventory.features.statistics.presentation.screens.StatisticsScreen

@Composable
fun NavigationWrapper(cameraManager: CameraManager) {
    val navController = rememberNavController()
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) InventoryHome else Login

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(InventoryHome) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Register) }
            )
        }
        composable<Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(InventoryHome) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable<InventoryHome> {
            InventoryHomeScreen(
                onAddProductClick = { navController.navigate(AddProduct) },
                onProductClick = { productId ->
                    navController.navigate(EditProduct(productId))
                },
                onStatisticsClick = { navController.navigate(Statistics) },
                onAlertsClick = { navController.navigate(Alerts) },
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(InventoryHome) { inclusive = true }
                    }
                }
            )
        }
        composable<AddProduct> { backStackEntry ->
            AddProductScreen(
                onBack = { navController.popBackStack() },
                onOpenCamera = { navController.navigate(Camera) },
                navBackStackEntry = backStackEntry
            )
        }
        composable<Camera> {
            CameraScreen(
                cameraManager = cameraManager,
                onPhotoTaken = { uri ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("photo_uri", uri.toString())
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<EditProduct> { backStackEntry ->
            val route = backStackEntry.toRoute<EditProduct>()
            EditProductScreen(
                productId = route.productId,
                onBack = { navController.popBackStack() },
                onOpenCamera = { navController.navigate(Camera) },
                navBackStackEntry = backStackEntry
            )
        }
        composable<Statistics> {
            StatisticsScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable<Alerts> {
            AlertsScreen(onBack = { navController.popBackStack() })
        }
    }
}