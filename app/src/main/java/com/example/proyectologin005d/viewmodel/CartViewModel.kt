package com.example.proyectologin005d.viewmodel

import androidx.lifecycle.ViewModel
import com.example.proyectologin005d.data.local.CatalogoProductoJson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartItem(val product: CatalogoProductoJson, val quantity: Int)

class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addToCart(product: CatalogoProductoJson) {
        _uiState.update { currentState ->
            val cart = currentState.cart.toMutableList()
            val existingItem = cart.find { it.product.id == product.id }
            if (existingItem != null) {
                val index = cart.indexOf(existingItem)
                cart[index] = existingItem.copy(quantity = existingItem.quantity + 1)
            } else {
                cart.add(CartItem(product, 1))
            }
            currentState.copy(cart = cart)
        }
    }

    fun removeFromCart(product: CatalogoProductoJson) {
        _uiState.update { currentState ->
            val cart = currentState.cart.toMutableList()
            val existingItem = cart.find { it.product.id == product.id }
            if (existingItem != null) {
                if (existingItem.quantity > 1) {
                    val index = cart.indexOf(existingItem)
                    cart[index] = existingItem.copy(quantity = existingItem.quantity - 1)
                } else {
                    cart.remove(existingItem)
                }
            }
            currentState.copy(cart = cart)
        }
    }

    fun clearCart() {
        _uiState.update { currentState ->
            currentState.copy(cart = emptyList())
        }
    }

    data class CartUiState(val cart: List<CartItem> = emptyList()) {
        val total: Int
            get() = cart.sumOf { it.product.precio * it.quantity }
    }
}
