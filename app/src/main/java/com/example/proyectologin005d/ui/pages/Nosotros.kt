package com.example.proyectologin005d.ui.pages

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectologin005d.ui.pages.common.AnimatedContent
import java.io.IOException

@Composable
fun assetToBitmap(assetPath: String): ImageBitmap? {
    val context = LocalContext.current
    return remember(assetPath) {
        try {
            context.assets.open(assetPath).use {
                BitmapFactory.decodeStream(it)
            }.asImageBitmap()
        } catch (e: IOException) {
            null
        }
    }
}

@Composable
fun Nosotros() {
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Nosotros",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Conoce más sobre nuestra historia, misión y visión como Pastelería 1000 Sabores.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        AnimatedContent {
            assetToBitmap("img/local.jpg")?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Fachada de la pastelería",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        AnimatedContent {
            Column {
                Text(
                    text = "Nuestra Historia",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = """
Pastelería 1000 Sabores celebra su 50° aniversario como un referente en la repostería chilena. 
Famosa por su participación en un récord Guinness en 1995, cuando colaboró en la creación de la torta más grande del mundo. 
Hoy continuamos innovando para mantener viva nuestra tradición dulce y artesanal.
""" .trimIndent(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        AnimatedContent {
            assetToBitmap("img/tienda.png")?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Equipo de pasteleros",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        AnimatedContent {
            Column {
                Text(
                    text = "Nuestra Misión",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = """
Ofrecer una experiencia dulce y memorable a nuestros clientes, elaborando productos de repostería de alta calidad para todas las ocasiones. 
Celebramos nuestras raíces y fomentamos la creatividad para que cada torta y postre sea una obra única.
""" .trimIndent(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        AnimatedContent {
            assetToBitmap("img/vitrina.png")?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Vitrina de pasteles",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(vertical = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        AnimatedContent {
            Column {
                Text(
                    text = "Nuestra Visión",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = """
Convertirnos en la tienda online líder en repostería en Chile, reconocida por nuestra innovación, calidad y compromiso con la comunidad. 
Buscamos inspirar a nuevos talentos en el mundo de la gastronomía y seguir siendo un símbolo de sabor y tradición.
""" .trimIndent(),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(bottom = 32.dp, top = 8.dp)
                )
            }
        }
    }
}
