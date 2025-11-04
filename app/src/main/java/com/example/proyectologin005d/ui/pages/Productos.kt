package com.example.proyectologin005d.ui.pages

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectologin005d.data.local.CatalogoProductoJson
import com.example.proyectologin005d.data.local.JsonReader
import com.example.proyectologin005d.viewmodel.CartViewModel
import kotlinx.coroutines.delay

private fun assetUrlFromJsonPath(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://") || path.startsWith("https://")) return path
    val cleaned = path.removePrefix("../assets/").removePrefix("./assets/").removePrefix("assets/").removePrefix("/")
    return "file:///android_asset/img/${Uri.encode(cleaned)}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(cartViewModel: CartViewModel = viewModel(), navController: NavController) {
    val context = LocalContext.current
    var productos by remember { mutableStateOf<List<CatalogoProductoJson>>(emptyList()) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        productos = JsonReader.cargarCatalogo(context)
        expanded = true
    }

    val cardPadding by animateDpAsState(
        targetValue = if (expanded) 12.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = ""
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("Catálogo Mil Sabores") }) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            items(productos, key = { it.id }) { p ->
                var added by remember { mutableStateOf(false) }
                val buttonColor by animateColorAsState(
                    targetValue = if (added) Color(0xFFC8E6C9) else Color(0xFFFFD1DC),
                    label = "buttonColor"
                )

                Card(Modifier.fillMaxWidth().padding(bottom = cardPadding)) {
                    Column(Modifier.padding(12.dp)) {
                        AsyncImage(
                            model = assetUrlFromJsonPath(p.imagen),
                            contentDescription = p.nombre,
                            modifier = Modifier.fillMaxWidth().height(160.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(p.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("Precio: CLP ${p.precio}")
                        Text("Categoría: ${p.categoria ?: "-"}")
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(onClick = { navController.navigate("productDetail/${p.id}") }) {
                                Text("Ver detalle")
                            }

                            Button(
                                onClick = {
                                    if (!added) {
                                        cartViewModel.addToCart(p)
                                        added = true
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                            ) {
                                if (added) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Filled.Check, contentDescription = "Agregado")
                                        Spacer(Modifier.width(8.dp))
                                        Text("Agregado", color = Color.Black)
                                    }
                                } else {
                                    Text("Agregar al carrito", color = Color.Black)
                                }
                            }
                        }
                    }
                }

                if (added) {
                    LaunchedEffect(p.id, added) {
                        delay(2000)
                        added = false
                    }
                }
            }
        }
    }
}
