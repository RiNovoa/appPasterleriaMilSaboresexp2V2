package com.example.proyectologin005d.ui.pages

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.proyectologin005d.R
import com.example.proyectologin005d.data.model.User
import com.example.proyectologin005d.data.repository.AuthRepository
import com.example.proyectologin005d.ui.pages.common.AnimatedContent
import kotlinx.coroutines.launch

@Composable
fun PerfilScreen(navController: NavController) {
    val cs = MaterialTheme.colorScheme
    val ty = MaterialTheme.typography
    val context = LocalContext.current
    val authRepository = remember { AuthRepository(context) }
    var user by remember { mutableStateOf<User?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageBitmap = if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        imageBitmap = bitmap
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch()
        }
    }

    LaunchedEffect(Unit) {
        authRepository.getSessionUsername()?.let {
            user = authRepository.getUserByUsername(it)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = cs.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent { 
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val imageModifier = Modifier
                            .size(130.dp)
                            .clip(CircleShape)
                            .border(4.dp, cs.primary, CircleShape)
                            .background(cs.secondary.copy(alpha = 0.1f))

                        if (imageBitmap != null) {
                            Image(
                                bitmap = imageBitmap!!.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                modifier = imageModifier,
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.ic_default_profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = imageModifier.padding(12.dp)
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Text(
                            text = user?.nombre ?: "Nombre de Usuario",
                            style = ty.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = cs.onSurfaceVariant
                        )

                        Text(
                            text = user?.correo ?: "correo@ejemplo.com",
                            style = ty.bodyMedium,
                            color = cs.onSurfaceVariant.copy(alpha = 0.7f)
                        )

                        Spacer(Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                        ) {
                            OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = "Galería", Modifier.size(18.dp))
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Galería")
                            }
                            Button(onClick = {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch()
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Cámara", Modifier.size(18.dp))
                                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                Text("Cámara")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            AnimatedContent { 
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Beneficios Activos",
                            style = ty.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = cs.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        HorizontalDivider(color = cs.outline.copy(alpha = 0.5f))
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "• 10% de descuento permanente (CUPÓN: FELICES50)\n• Regalo especial de cumpleaños al registrar correo Duoc.",
                            style = ty.bodyMedium,
                            lineHeight = 24.sp,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            AnimatedContent {
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {
                        scope.launch {
                            authRepository.logout()
                            navController.navigate("login") { popUpTo(0) { inclusive = true } }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = cs.errorContainer,
                        contentColor = cs.onErrorContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(50.dp)
                ) {
                    Text("Cerrar Sesión", style = ty.titleMedium)
                }
            }
        }
    }
}
