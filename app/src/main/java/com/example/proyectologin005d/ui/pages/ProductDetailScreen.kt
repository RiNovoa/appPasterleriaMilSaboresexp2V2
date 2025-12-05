package com.example.proyectologin005d.ui.pages

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectologin005d.data.database.ProductoDataBase
import com.example.proyectologin005d.data.local.CatalogoProductoJson
import com.example.proyectologin005d.data.local.JsonReader
import com.example.proyectologin005d.data.model.Resena
import com.example.proyectologin005d.ui.pages.common.AnimatedContent
import kotlinx.coroutines.launch

private fun assetUrlFromJsonPath(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://") || path.startsWith("https://")) return path
    val cleaned = path.removePrefix("../assets/").removePrefix("./assets/").removePrefix("assets/").removePrefix("/")
    return "file:///android_asset/img/${Uri.encode(cleaned)}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: Int) {
    val context = LocalContext.current
    var product by remember { mutableStateOf<CatalogoProductoJson?>(null) }

    LaunchedEffect(productId) {
        val allProducts = JsonReader.cargarCatalogo(context)
        product = allProducts.find { it.id == productId }
    }

    Scaffold(
        topBar = {
            AnimatedContent {
                TopAppBar(
                    title = { Text(product?.nombre ?: "Detalle del Producto") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        }
    ) { padding ->
        product?.let { p ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedContent {
                    AsyncImage(
                        model = assetUrlFromJsonPath(p.imagen),
                        contentDescription = p.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(Modifier.height(16.dp))
                AnimatedContent {
                    Text(p.nombre, style = MaterialTheme.typography.headlineMedium)
                }
                Spacer(Modifier.height(8.dp))
                AnimatedContent {
                    Text("Precio: CLP ${p.precio}", style = MaterialTheme.typography.titleLarge)
                }
                Spacer(Modifier.height(8.dp))
                AnimatedContent {
                    Text("Categoría: ${p.categoria ?: "-"}", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(8.dp))
                AnimatedContent {
                    Text("Stock disponible: ${p.stock}", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(16.dp))
                AnimatedContent {
                    Text(p.descripcion ?: "No hay descripción disponible.", style = MaterialTheme.typography.bodyLarge)
                }
                Spacer(Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))
                AnimatedContent {
                    ReviewsSection(productId = p.id)
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ReviewsSection(productId: Int) {
    var rating by remember { mutableStateOf(0) }
    var reviewText by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val database = remember { ProductoDataBase.getDatabase(context) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Calificaciones y Reseñas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Tu calificación:")
            Spacer(modifier = Modifier.width(8.dp))
            RatingBar(rating = rating, onRatingChanged = { rating = it })
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            label = { Text("Escribe tu reseña") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            if (rating > 0 && reviewText.isNotBlank()) {
                scope.launch {
                    val resena = Resena(productId = productId, calificacion = rating, comentario = reviewText)
                    database.resenaDao().insertResena(resena)
                    
                    // Reset fields and show confirmation
                    rating = 0
                    reviewText = ""
                    Toast.makeText(context, "Reseña enviada", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Por favor, ingresa una calificación y un comentario.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Enviar reseña")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("Otras reseñas (próximamente)", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun RatingBar(rating: Int, onRatingChanged: (Int) -> Unit) {
    Row {
        (1..5).forEach { index ->
            Icon(
                imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = "Estrella $index",
                tint = Color(0xFFFFC107),
                modifier = Modifier.clickable { onRatingChanged(index) }
            )
        }
    }
}
