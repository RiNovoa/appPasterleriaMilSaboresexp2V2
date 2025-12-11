package com.example.proyectologin005d.ui.pages

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.proyectologin005d.data.model.Producto
import com.example.proyectologin005d.data.repository.ProductoRepository
import com.example.proyectologin005d.ui.pages.common.AnimatedContent
import com.example.proyectologin005d.viewmodel.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

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
    val repository = remember { ProductoRepository(context) }
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        repository.obtenerProductos().collectLatest {
            productos = it
        }
    }

    val categories = productos.mapNotNull { it.categoria }.distinct()

    val filteredProducts = productos.filter {
        (selectedCategory == null || it.categoria == selectedCategory) &&
                (searchQuery.isBlank() || it.nombre.contains(searchQuery.trim(), ignoreCase = true))
    }

    Scaffold(
        topBar = {
            AnimatedContent {
                TopAppBar(title = { Text("Catálogo Mil Sabores") })
            }
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding).animateContentSize()) {
            ProductSearchField(
                query = searchQuery, 
                onQueryChange = { searchQuery = it },
                onClear = { searchQuery = "" }
            )
            CategoryFilters(categories, selectedCategory) { selectedCategory = it }
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                if (filteredProducts.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            Text("No se encontraron productos", color = Color.Gray)
                        }
                    }
                } else {
                    items(filteredProducts, key = { it.id }) { p ->
                        ProductItem(p, cartViewModel, navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchField(query: String, onQueryChange: (String) -> Unit, onClear: () -> Unit) {
    val focusManager = LocalFocusManager.current
    
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Buscar producto") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Close, contentDescription = "Borrar")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilters(categories: List<String>, selectedCategory: String?, onCategorySelected: (String?) -> Unit) {
    Row(modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp)) {
        FilterChip(
            selected = selectedCategory == null,
            onClick = { onCategorySelected(null) },
            label = { Text("Todos") },
            modifier = Modifier.padding(end = 8.dp)
        )
        categories.forEach {
            FilterChip(
                selected = selectedCategory == it,
                onClick = { onCategorySelected(it) },
                label = { Text(it) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun ProductItem(p: Producto, cartViewModel: CartViewModel, navController: NavController) {
    var added by remember { mutableStateOf(false) }
    val buttonColor by animateColorAsState(
        targetValue = if (added) Color(0xFFC8E6C9) else Color(0xFFFFD1DC),
        label = "buttonColor"
    )

    Card(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
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
            Text("Stock: ${p.stock}")
            Spacer(Modifier.height(8.dp))
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
                        if (!added && p.stock > 0) {
                            cartViewModel.addToCart(p)
                            added = true
                        }
                    },
                    enabled = p.stock > 0,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    if (p.stock <= 0) {
                        Text("Agotado", color = Color.Gray)
                    } else if (added) {
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
