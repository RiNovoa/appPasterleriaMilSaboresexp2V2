package com.example.proyectologin005d.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.proyectologin005d.ui.pages.common.AnimatedContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(navController: NavController) {
    Scaffold(
        topBar = {
            AnimatedContent {
                TopAppBar(title = { Text("Historial de Pedidos") })
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Aquí mostraremos el historial de pedidos
            Text("Próximamente: ¡Aquí verás tus pedidos anteriores!")
        }
    }
}
