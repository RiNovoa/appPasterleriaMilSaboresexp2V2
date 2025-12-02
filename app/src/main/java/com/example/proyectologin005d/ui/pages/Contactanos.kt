package com.example.proyectologin005d.ui.pages

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proyectologin005d.ui.pages.common.AnimatedContent

@Composable
fun ContactoScreen() {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Contacto",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = "¿Tienes alguna pregunta o quieres encargar un pastel? Escríbenos y te responderemos pronto.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        AnimatedContent {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }

        AnimatedContent {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }

        AnimatedContent {
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text("Mensaje") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 4.dp)
            )
        }

        AnimatedContent {
            Button(
                onClick = { /* Aquí puedes manejar el envío */ },
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB6C1))
            ) {
                Text("Enviar mensaje", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        AnimatedContent {
            Column {
                Text(
                    text = "Nuestra tienda",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ContactInfo(label = "Dirección:", value = "Av. Concha y Toro 1340, Puente Alto")
                ContactInfo(label = "Teléfono:", value = "+56 9 1234 5678")
                ContactInfo(label = "Horario:", value = "Lun-Sab 9:00 - 20:00")
            }
        }
        
        // Mapa embebido usando WebView
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent {
            Text(
                text = "Ubicación",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp).align(Alignment.Start)
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
            ) {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            webViewClient = WebViewClient()
                            settings.javaScriptEnabled = true
                            // HTML embebido con iframe de Google Maps
                            val iframe = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3325.661079387937!2d-70.5853926243043!3d-33.53619397335603!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9662d0a103328057%3A0x6d2224550f0895c9!2sDuoc%20UC%3A%20Sede%20Puente%20Alto!5e0!3m2!1ses!2scl!4v1716414032015!5m2!1ses!2scl\" width=\"100%\" height=\"100%\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>"
                            loadData(iframe, "text/html", "utf-8")
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        AnimatedContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Contáctanos en redes",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SocialButton("Instagram", Color(0xFFE1306C)) {
                        uriHandler.openUri("https://www.instagram.com")
                    }
                    SocialButton("WhatsApp", Color(0xFF25D366)) {
                        uriHandler.openUri("https://wa.me/56912345678")
                    }
                    SocialButton("Facebook", Color(0xFF1877F2)) {
                        uriHandler.openUri("https://www.facebook.com")
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInfo(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(
            text = "$label ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontSize = 16.sp
        )
    }
}

@Composable
fun SocialButton(text: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
