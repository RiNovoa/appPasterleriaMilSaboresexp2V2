package com.example.proyectologin005d.data.repository

import android.content.Context
import com.example.proyectologin005d.data.database.ProductoDataBase
import com.example.proyectologin005d.data.local.JsonReader
import com.example.proyectologin005d.data.model.CartItemEntity
import com.example.proyectologin005d.data.model.OrderEntity
import com.example.proyectologin005d.data.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductoRepository(private val context: Context) {

    // Use lazy initialization to prevent main thread access during object creation if possible,
    // though Room builder is fine on main thread, accessing DAO is better deferred.
    private val database by lazy { ProductoDataBase.getDatabase(context) }
    private val productoDao by lazy { database.productoDao() }
    private val cartDao by lazy { database.cartDao() }
    private val orderDao by lazy { database.orderDao() }

    // Inicializar la base de datos con los datos del JSON si está vacía
    init {
        // We need to be careful here. If we access productoDao immediately, it might trigger DB creation.
        // Ideally, this should be called from a suspend function or View Model init, not Repository init.
        // However, to keep it simple and working as intended:
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (productoDao.obtenerPorId(1) == null) {
                    cargarDatosIniciales()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun cargarDatosIniciales() {
        try {
            val catalogoJson = JsonReader.cargarCatalogo(context)
            val productosEntidad = catalogoJson.map { Producto.fromJson(it) }
            productoDao.insertarTodos(productosEntidad)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun obtenerProductos(): Flow<List<Producto>> {
        return productoDao.obtenerTodos()
    }

    suspend fun obtenerProductoPorId(id: Int): Producto? {
        return productoDao.obtenerPorId(id)
    }
    
    suspend fun actualizarStock(producto: Producto) {
        productoDao.actualizar(producto)
    }

    // Cart methods
    fun obtenerCarrito(): Flow<List<CartItemEntity>> {
        return cartDao.obtenerCarrito()
    }

    suspend fun agregarAlCarrito(item: CartItemEntity) {
        cartDao.agregar(item)
    }

    suspend fun eliminarDelCarrito(productoId: Int) {
        cartDao.eliminarProducto(productoId)
    }

    suspend fun vaciarCarrito() {
        cartDao.vaciar()
    }

    // Order methods
    fun obtenerOrdenes(): Flow<List<OrderEntity>> {
        return orderDao.obtenerTodas()
    }

    suspend fun guardarOrden(order: OrderEntity) {
        orderDao.insertar(order)
    }
    
    fun obtenerUltimaOrden(): Flow<OrderEntity?> {
        return orderDao.obtenerUltimaOrden()
    }
}
