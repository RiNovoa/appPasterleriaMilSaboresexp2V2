
package com.example.proyectologin005d.ui.pages

import android.content.Context
import android.net.Uri
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectologin005d.data.Pastel
import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

@Composable
fun IndexScreen(navController: NavController) {
    val context = LocalContext.current
    val pasteles = remember { loadPasteles(context) }
    val scroll = rememberScrollState()

    var imageVisible by remember { mutableStateOf(false) }
    val imageAlpha by animateFloatAsState(
        targetValue = if (imageVisible) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(Unit) {
        delay(200)
        imageVisible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .background(Color(0xFFFFF8F2))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado con logo
        Image(
            painter = rememberAsyncImagePainter(model = "file:///android_asset/img/logo.png"),
            contentDescription = "Logo Pastelería 1000 Sabores",
            modifier = Modifier
                .height(120.dp)
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Pastelería 1000 Sabores",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF4A3C2A),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Imagen destacada
        Image(
            painter = rememberAsyncImagePainter(model = "file:///android_asset/img/local.jpg"),
            contentDescription = "Fachada de la tienda",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .alpha(imageAlpha),
            contentScale = ContentScale.Crop
        )

        // Mensaje de bienvenida
        var textVisible by remember { mutableStateOf(false) }
        val textScale by animateFloatAsState(
            targetValue = if (textVisible) 1f else 0.8f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )

        LaunchedEffect(Unit) {
            delay(400) // Delay to start after image
            textVisible = true
        }

        Text(
            text = "Bienvenidos al dulce mundo de 1000 Sabores. " +
                    "Aquí cada pastel es una historia, cada sabor un recuerdo y cada visita una sonrisa.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF5E5E5E),
            modifier = Modifier
                .padding(vertical = 12.dp)
                .graphicsLayer {
                    scaleX = textScale
                    scaleY = textScale
                }
        )

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(vertical = 20.dp)
        )

        // Sección de productos destacados
        Text(
            text = "Destacados de la semana",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A3C2A),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        pasteles.take(4).forEachIndexed { index, pastel ->
            var cardVisible by remember { mutableStateOf(false) }
            val cardOffset by animateFloatAsState(
                targetValue = if (cardVisible) 0f else 100f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessVeryLow
                ),
                label = ""
            )

            LaunchedEffect(Unit) {
                delay(200L * index)
                cardVisible = true
            }

            ProductCard(
                image = pastel.imagen,
                nombre = pastel.nombre,
                precio = "$${pastel.precio}",
                descripcion = pastel.descripcion,
                modifier = Modifier.offset(y = cardOffset.dp)
            )
        }

        HorizontalDivider(
            color = Color(0xFFE0E0E0),
            modifier = Modifier.padding(vertical = 20.dp)
        )

        // Mensaje final
        Text(
            text = "Hecho con amor desde 1975 💖",
            fontSize = 14.sp,
            color = Color(0xFF8D6E63),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
fun ProductCard(image: String, nombre: String, precio: String, descripcion: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "file:///android_asset/img/" + Uri.encode(image)),
                contentDescription = nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4A3C2A),
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = precio,
                fontSize = 16.sp,
                color = Color(0xFFD81B60),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = descripcion,
                fontSize = 14.sp,
                color = Color(0xFF5E5E5E),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

fun loadPasteles(context: Context): List<Pastel> {
    val jsonString: String
    try {
        jsonString = context.assets.open("database/Pasteles.json").bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return emptyList()
    }
    return Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
}
