package com.example.proyectologin005d.ui.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.proyectologin005d.viewmodel.CartViewModel

class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.take(16) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i < 15) out += " "
        }
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }
        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.take(4) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i == 1) out += "/"
        }
        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }
        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, cartViewModel: CartViewModel) {
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var isCvvFocused by remember { mutableStateOf(false) }
    var discountCodeInput by remember { mutableStateOf("") }

    var cardNameError by remember { mutableStateOf<String?>(null) }
    var cardNumberError by remember { mutableStateOf<String?>(null) }
    var expiryDateError by remember { mutableStateOf<String?>(null) }
    var cvvError by remember { mutableStateOf<String?>(null) }

    val appliedPercentage by cartViewModel.discountPercentage.collectAsState()
    val appliedCode by cartViewModel.discountCode.collectAsState()
    val uiState by cartViewModel.uiState.collectAsState()

    val isFormValid by remember {
        derivedStateOf {
            cardName.isNotBlank() && cardNumber.length == 16 && expiryDate.length == 4 && cvv.length in 3..4 &&
                    cardNameError == null && cardNumberError == null && expiryDateError == null && cvvError == null
        }
    }

    fun validateCardName() {
        cardNameError = if (cardName.isBlank()) "El nombre no puede estar vacío" else null
    }

    fun validateCardNumber() {
        cardNumberError = when {
            cardNumber.isBlank() -> "El número no puede estar vacío"
            cardNumber.length != 16 -> "Debe tener 16 dígitos"
            else -> null
        }
    }

    fun validateExpiryDate() {
        expiryDateError = when {
            expiryDate.isBlank() -> "La fecha no puede estar vacía"
            expiryDate.length != 4 -> "Debe tener 4 dígitos (MMAA)"
            else -> {
                val month = expiryDate.take(2).toIntOrNull()
                if (month == null || month !in 1..12) {
                    "Mes inválido"
                } else {
                    null
                }
            }
        }
    }

    fun validateCvv() {
        cvvError = when {
            cvv.isBlank() -> "El CVV no puede estar vacío"
            cvv.length !in 3..4 -> "Debe tener 3 o 4 dígitos"
            else -> null
        }
    }

    val rotation by animateFloatAsState(targetValue = if (isCvvFocused) 180f else 0f, label = "")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 8 * density
                },
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF6F1E51), Color(0xFFED4C67), Color(0xFFF79F1F)),
                        )
                    )
            ) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                    if (rotation < 90f) {
                        Image(painter = rememberAsyncImagePainter(model = "file:///android_asset/img/logo.png"), contentDescription = null, modifier = Modifier.height(40.dp))
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = CardNumberVisualTransformation().filter(AnnotatedString(cardNumber)).text.text, color = Color.White)
                        Row {
                            Text(text = cardName.ifEmpty { "NOMBRE APELLIDO" }, color = Color.White)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(text = ExpiryDateVisualTransformation().filter(AnnotatedString(expiryDate)).text.text, color = Color.White)
                        }
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                            Text(text = "CVV: ${cvv.ifEmpty { "###" }}", color = Color.White)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cardName,
            onValueChange = { cardName = it; validateCardName() },
            label = { Text("Nombre en la tarjeta") },
            modifier = Modifier.fillMaxWidth(),
            isError = cardNameError != null,
            supportingText = { if (cardNameError != null) Text(cardNameError!!) }
        )

        OutlinedTextField(
            value = cardNumber,
            onValueChange = {
                if (it.length <= 16) {
                    cardNumber = it.filter { char -> char.isDigit() }
                    validateCardNumber()
                }
            },
            label = { Text("Número de tarjeta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = CardNumberVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = cardNumberError != null,
            supportingText = { if (cardNumberError != null) Text(cardNumberError!!) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = {
                    if (it.length <= 4) {
                        expiryDate = it.filter { char -> char.isDigit() }
                        validateExpiryDate()
                    }
                },
                label = { Text("Expira (MM/AA)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = ExpiryDateVisualTransformation(),
                modifier = Modifier.weight(1f),
                isError = expiryDateError != null,
                supportingText = { if (expiryDateError != null) Text(expiryDateError!!) }
            )
            Spacer(modifier = Modifier.width(16.dp))
            OutlinedTextField(
                value = cvv,
                onValueChange = {
                    if (it.length <= 4) {
                        cvv = it.filter { char -> char.isDigit() }
                        validateCvv()
                    }
                },
                label = { Text("CVV") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { isCvvFocused = it.isFocused },
                isError = cvvError != null,
                supportingText = { if (cvvError != null) Text(cvvError!!) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de cupón de descuento
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Cupón de descuento", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = discountCodeInput,
                        onValueChange = { discountCodeInput = it },
                        label = { Text("Código") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = { cartViewModel.applyCoupon(discountCodeInput) }) {
                        Text("Aplicar")
                    }
                }
                
                if (appliedPercentage > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "¡Descuento del ${(appliedPercentage * 100).toInt()}% aplicado!",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Subtotal:")
                    Text("$${uiState.total}")
                }
                if (appliedPercentage > 0) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Descuento:", color = Color(0xFF4CAF50))
                        Text("-$${(uiState.total * appliedPercentage).toInt()}", color = Color(0xFF4CAF50))
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total a Pagar:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    val finalTotal = (uiState.total * (1 - appliedPercentage)).toInt()
                    Text("$$finalTotal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                cartViewModel.checkout()
                navController.navigate("boleta")
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD1DC))
        ) {
            Text("Pagar", color = Color.Black)
        }
    }
}
