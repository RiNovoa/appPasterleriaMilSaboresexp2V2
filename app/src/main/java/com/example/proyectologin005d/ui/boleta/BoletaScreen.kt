package com.example.proyectologin005d.ui.boleta

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectologin005d.ui.pages.common.AnimatedContent
import com.example.proyectologin005d.viewmodel.CartViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoletaScreen(navController: NavController, cartViewModel: CartViewModel) {
    val cartState by cartViewModel.uiState.collectAsState()
    val currentDate = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }
    val orderNumber = remember { (1000..9999).random() }

    Scaffold(
        topBar = {
            AnimatedContent {
                TopAppBar(title = { Text("Boleta Electrónica") })
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = rememberAsyncImagePainter(model = "file:///android_asset/img/logo.png"),
                        contentDescription = "Logo Pastelería 1000 Sabores",
                        modifier = Modifier.height(80.dp)
                    )
                    Text("Pastelería 1000 Sabores", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("¡Gracias por su compra!", style = MaterialTheme.typography.titleMedium)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Fecha: $currentDate", fontSize = 14.sp)
                        Text("Pedido N°: $orderNumber", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Resumen de la compra", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                    
                    LazyColumn(modifier = Modifier.heightIn(max = 250.dp)) {
                        items(cartState.cart) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("${item.quantity}x ${item.product.nombre}")
                                Text("$${item.product.precio * item.quantity}")
                            }
                        }
                    }
                    
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total pagado:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("$${cartState.total}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedContent {
                Button(onClick = {
                    cartViewModel.clearCart()
                    navController.navigate("index") { popUpTo(0) }
                }) {
                    Text("Volver al inicio")
                }
            }
        }
    }
}
