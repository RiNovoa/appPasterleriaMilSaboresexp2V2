package com.example.proyectologin005d.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.local.CatalogoProductoJson
import com.example.proyectologin005d.data.model.CartItem
import com.example.proyectologin005d.data.model.CartItemEntity
import com.example.proyectologin005d.data.model.Order
import com.example.proyectologin005d.data.model.OrderEntity
import com.example.proyectologin005d.data.model.Producto
import com.example.proyectologin005d.data.repository.ProductoRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CartViewModel(
    application: Application,
    private val repository: ProductoRepository
) : AndroidViewModel(application) {

    constructor(application: Application) : this(application, ProductoRepository(application))
    
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _lastOrder = MutableStateFlow<Order?>(null)
    val lastOrder: StateFlow<Order?> = _lastOrder.asStateFlow()
    
    private val _discountCode = MutableStateFlow("")
    val discountCode: StateFlow<String> = _discountCode.asStateFlow()

    private val _discountPercentage = MutableStateFlow(0.0)
    val discountPercentage: StateFlow<Double> = _discountPercentage.asStateFlow()

    init {
        viewModelScope.launch {
            repository.obtenerCarrito().collectLatest { cartEntities ->
                val cartItems = mutableListOf<CartItem>()
                cartEntities.forEach { entity ->
                     val product = repository.obtenerProductoPorId(entity.productoId)
                     if (product != null) {
                         cartItems.add(CartItem(product, entity.cantidad))
                     }
                }
                _uiState.update { it.copy(cart = cartItems) }
            }
        }
        
        viewModelScope.launch {
            repository.obtenerOrdenes().collectLatest { orderEntities ->
                val gson = Gson()
                val type = object : TypeToken<List<CartItem>>() {}.type
                
                val mappedOrders = orderEntities.map { entity ->
                    val items: List<CartItem> = try {
                         gson.fromJson(entity.itemsJson, type)
                    } catch (e: Exception) {
                        emptyList()
                    }
                    
                    Order(
                        id = entity.id,
                        date = entity.date,
                        items = items,
                        total = entity.total
                    )
                }
                _orders.value = mappedOrders
            }
        }
        
        viewModelScope.launch {
            repository.obtenerUltimaOrden().collectLatest { entity ->
                if (entity != null) {
                    val gson = Gson()
                    val type = object : TypeToken<List<CartItem>>() {}.type
                    val items: List<CartItem> = try {
                         gson.fromJson(entity.itemsJson, type)
                    } catch (e: Exception) {
                        emptyList()
                    }
                    
                    _lastOrder.value = Order(
                        id = entity.id,
                        date = entity.date,
                        items = items,
                        total = entity.total
                    )
                }
            }
        }
    }
    
    fun addToCart(productJson: CatalogoProductoJson) {
        val product = Producto.fromJson(productJson)
        addToCart(product)
    }
    
    fun addToCart(product: Producto) {
        viewModelScope.launch {
            // Verificar si ya está en el carrito para sumar cantidad
            val currentCart = _uiState.value.cart
            val existingItem = currentCart.find { it.product.id == product.id }
            
            if (existingItem != null) {
                // En la base de datos local, por simplicidad, eliminamos y agregamos con nueva cantidad
                // O idealmente CartDao debería tener update o upsert.
                // Como CartItemEntity no tiene ID único fijo aparte de autogenerado, 
                // es mejor borrar por productoId y agregar de nuevo o buscar la entidad.
                // Sin embargo, para simplificar:
                repository.eliminarDelCarrito(product.id)
                repository.agregarAlCarrito(CartItemEntity(productoId = product.id, cantidad = existingItem.quantity + 1))
            } else {
                repository.agregarAlCarrito(CartItemEntity(productoId = product.id, cantidad = 1))
            }
        }
    }

    fun removeFromCart(productJson: CatalogoProductoJson) {
        val product = Producto.fromJson(productJson)
        removeFromCart(product)
    }

    fun removeFromCart(product: Producto) {
        viewModelScope.launch {
             val currentCart = _uiState.value.cart
            val existingItem = currentCart.find { it.product.id == product.id }
            
            if (existingItem != null && existingItem.quantity > 1) {
                 repository.eliminarDelCarrito(product.id)
                 repository.agregarAlCarrito(CartItemEntity(productoId = product.id, cantidad = existingItem.quantity - 1))
            } else {
                repository.eliminarDelCarrito(product.id)
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.vaciarCarrito()
        }
    }

    fun applyCoupon(code: String) {
        _discountCode.value = code
        if (code.trim().equals("Pasteleria50", ignoreCase = true)) {
            _discountPercentage.value = 0.30 // 30% de descuento
        } else {
            _discountPercentage.value = 0.0
        }
    }

    fun checkout() {
        val currentState = _uiState.value
        val subtotal = currentState.total
        val discount = (subtotal * _discountPercentage.value).toInt()
        val finalTotal = subtotal - discount

        if (currentState.cart.isNotEmpty()) {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentDate = sdf.format(Date())
            
            val gson = Gson()
            val itemsJson = gson.toJson(currentState.cart)

            val newOrder = OrderEntity(
                date = currentDate,
                total = finalTotal,
                itemsJson = itemsJson
            )

            viewModelScope.launch {
                repository.guardarOrden(newOrder)
                
                currentState.cart.forEach { item ->
                    val currentProduct = repository.obtenerProductoPorId(item.product.id)
                    if (currentProduct != null) {
                        val newStock = currentProduct.stock - item.quantity
                        if (newStock >= 0) {
                            repository.actualizarStock(currentProduct.copy(stock = newStock))
                        }
                    }
                }
                
                 repository.vaciarCarrito()
                 _discountCode.value = ""
                 _discountPercentage.value = 0.0
            }
        }
    }

    data class CartUiState(val cart: List<CartItem> = emptyList()) {
        val total: Int
            get() = cart.sumOf { it.product.precio * it.quantity }
    }
}
