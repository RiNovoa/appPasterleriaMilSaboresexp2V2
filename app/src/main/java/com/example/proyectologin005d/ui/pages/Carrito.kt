package com.example.proyectologin005d.ui.pages

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectologin005d.viewmodel.CartViewModel
import kotlinx.coroutines.launch

private fun assetUrlFromJsonPath(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://") || path.startsWith("https://")) return path
    val cleaned = path.removePrefix("../assets/").removePrefix("./assets/").removePrefix("assets/").removePrefix("/")
    return "file:///android_asset/img/${Uri.encode(cleaned)}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(cartViewModel: CartViewModel = viewModel(), navController: NavController) {
    val cartState by cartViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito de Compras") }) },
        bottomBar = {
            BottomAppBar {
                Text(text = "Total: ${cartState.total}")
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { navController.navigate("payment") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD1DC))
                ) {
                    Text("Pagar", color = Color.Black)
                }
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(cartState.cart, key = { it.product.id }) { item ->
                AnimatedVisibility(
                    visible = true, 
                    enter = slideInHorizontally(initialOffsetX = { -it }) + fadeIn(),
                    exit = fadeOut(animationSpec = spring())
                ) {
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = assetUrlFromJsonPath(item.product.imagen),
                                contentDescription = item.product.nombre,
                                modifier = Modifier.size(64.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = item.product.nombre)
                                Text(text = "$${item.product.precio}")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { cartViewModel.removeFromCart(item.product) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Remove")
                                }
                                Text(text = "${item.quantity}")
                                IconButton(onClick = { cartViewModel.addToCart(item.product) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = { scope.launch { cartViewModel.clearCart() } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD1DC))
                ) {
                    Text("Limpiar carrito", color = Color.Black)
                }
            }
        }
    }
}
