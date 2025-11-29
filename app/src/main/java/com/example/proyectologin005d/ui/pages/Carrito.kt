package com.example.proyectologin005d.ui.pages

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectologin005d.viewmodel.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun assetUrlFromJsonPath(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://") || path.startsWith("https://")) return path
    val cleaned = path.removePrefix("../assets/").removePrefix("./assets/").removePrefix("assets/").removePrefix("/")
    return "file:///android_asset/img/${Uri.encode(cleaned)}"
}

@Composable
fun AnimatedContent(content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = ""
    )
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = ""
    )

    LaunchedEffect(Unit) {
        delay(100)
        visible = true
    }

    Column(modifier = Modifier
        .graphicsLayer {
            this.alpha = alpha
            this.scaleX = scale
            this.scaleY = scale
        }
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(cartViewModel: CartViewModel = viewModel(), navController: NavController) {
    val cartState by cartViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AnimatedContent {
                TopAppBar(
                    title = { Text("Carrito de Compras") },
                    actions = {
                        AnimatedVisibility(visible = cartState.cart.isNotEmpty()) {
                            IconButton(onClick = { scope.launch { cartViewModel.clearCart() } }) {
                                Icon(Icons.Default.Delete, contentDescription = "Limpiar carrito")
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = cartState.cart.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                BottomAppBar {
                    Text("Total: $${cartState.total}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 16.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { navController.navigate("payment") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD1DC)),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text("Pagar", color = Color.Black)
                    }
                }
            }
        }
    ) { padding ->
        if (cartState.cart.isEmpty()) {
            EmptyCartView(navController)
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                itemsIndexed(cartState.cart, key = { _, item -> item.product.id }) { index, item ->
                    var visible by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(100L * index)
                        visible = true
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = slideInHorizontally(initialOffsetX = { -it / 2 }) + fadeIn(),
                        exit = fadeOut()
                    ) {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = assetUrlFromJsonPath(item.product.imagen),
                                    contentDescription = item.product.nombre,
                                    modifier = Modifier.size(64.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.product.nombre, fontWeight = FontWeight.Bold)
                                    Text("$${item.product.precio}")
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { cartViewModel.removeFromCart(item.product) }) {
                                        Icon(Icons.Default.Remove, contentDescription = "Remove")
                                    }
                                    Text("${item.quantity}", fontWeight = FontWeight.Bold)
                                    IconButton(onClick = { cartViewModel.addToCart(item.product) }) {
                                        Icon(Icons.Default.Add, contentDescription = "Add")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyCartView(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito vacío", modifier = Modifier.size(100.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tu carrito está vacío", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Parece que todavía no has añadido nada", textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("productos") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD1DC))
        ) {
            Text("Explorar productos", color = Color.Black)
        }
    }
}
