package com.joaquin.josuna_inventory.features.inventory.presentation.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.joaquin.josuna_inventory.features.alerts.presentation.viewmodels.AlertsViewModel
import com.joaquin.josuna_inventory.features.inventory.domain.entities.Product
import com.joaquin.josuna_inventory.features.inventory.presentation.components.ProductCard
import com.joaquin.josuna_inventory.features.inventory.presentation.viewmodels.InventoryViewModel
import com.joaquin.josuna_inventory.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun InventoryHomeScreen(
    onAddProductClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onStatisticsClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onLogout: () -> Unit = {},
    viewModel: InventoryViewModel = hiltViewModel(),
    alertsViewModel: AlertsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isRefreshing,
        onRefresh = viewModel::refresh
    )

    if (showDeleteDialog && selectedProduct != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar ${selectedProduct!!.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteProduct(selectedProduct!!.id)
                    showDeleteDialog = false
                    selectedProduct = null
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }

    if (selectedProduct != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedProduct = null },
            sheetState = sheetState,
            containerColor = Surface
        ) {
            ProductDetailSheet(
                product = selectedProduct!!,
                onEdit = {
                    val id = selectedProduct!!.id
                    selectedProduct = null
                    onProductClick(id)
                },
                onDelete = { showDeleteDialog = true }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "JOSUNA",
                            style = MaterialTheme.typography.labelMedium,
                            color = Primary,
                            letterSpacing = 4.sp
                        )
                        Text(
                            text = "Inventario",
                            style = MaterialTheme.typography.headlineSmall,
                            color = OnBackground
                        )
                    }
                },
                actions = {
                    // Badge de alertas
                    val alertsUiState by alertsViewModel.uiState.collectAsStateWithLifecycle()

                    BadgedBox(
                        badge = {
                            if (alertsUiState.alertCount > 0) {
                                Badge(containerColor = Error) {
                                    Text(
                                        "${alertsUiState.alertCount}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onAlertsClick) {
                            Icon(Icons.Default.Notifications, contentDescription = "Alertas", tint = OnSurfaceVariant)
                        }
                    }
                    IconButton(onClick = onStatisticsClick) {
                        Icon(Icons.Default.BarChart, contentDescription = "Estadísticas", tint = OnSurfaceVariant)
                    }
                    IconButton(onClick = { viewModel.logout(); onLogout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Salir", tint = OnSurfaceVariant)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProductClick,
                containerColor = Primary,
                contentColor = Color(0xFF0D0D0F),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar", modifier = Modifier.size(28.dp))
            }
        },
        containerColor = Background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // 🔍 Barra de búsqueda
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = viewModel::onSearchQueryChange,
                    placeholder = {
                        Text("Buscar producto...", color = OnSurfaceDim)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = OnSurfaceVariant)
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar", tint = OnSurfaceVariant)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = SurfaceBorder,
                        focusedTextColor = OnBackground,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Primary,
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Contador de resultados
                if (uiState.searchQuery.isNotEmpty()) {
                    Text(
                        text = "${uiState.filteredProducts.size} resultado(s)",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Primary)
                        }
                    }
                    uiState.filteredProducts.isEmpty() && uiState.searchQuery.isNotEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = OnSurfaceDim
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "Sin resultados para \"${uiState.searchQuery}\"",
                                    color = OnSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                    uiState.products.isEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Inventory2,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = OnSurfaceDim
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("No hay productos aún", style = MaterialTheme.typography.titleMedium,
                                    color = OnSurfaceVariant)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Toca + para agregar uno", style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurfaceDim)
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(uiState.filteredProducts, key = { it.id }) { product ->
                                ProductCard(
                                    product = product,
                                    onClick = { selectedProduct = product }
                                )
                            }
                        }
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = uiState.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = Surface,
                contentColor = Primary
            )
        }
    }
}

@Composable
private fun ProductDetailSheet(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 24.dp)
            .padding(bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto grande
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceElevated),
            contentAlignment = Alignment.Center
        ) {
            if (product.photoPath.isNotEmpty()) {
                AsyncImage(
                    model = Uri.parse(product.photoPath),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = OnSurfaceDim
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            color = OnBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Stats row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceElevated)
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("PRECIO", style = MaterialTheme.typography.labelMedium, color = OnSurfaceDim)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "$${product.price}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(SurfaceBorder)
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("STOCK", style = MaterialTheme.typography.labelMedium, color = OnSurfaceDim)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${product.quantity}",
                    style = MaterialTheme.typography.titleLarge,
                    color = OnBackground,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onDelete,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Error),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Error)
            ) {
                Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Eliminar", style = MaterialTheme.typography.labelLarge)
            }

            Button(
                onClick = onEdit,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp), tint = Color(0xFF0D0D0F))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Editar", style = MaterialTheme.typography.labelLarge, color = Color(0xFF0D0D0F))
            }
        }
    }
}
