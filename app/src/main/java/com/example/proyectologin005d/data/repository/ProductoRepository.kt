package com.example.proyectologin005d.data.repository

import android.content.Context
import com.example.proyectologin005d.data.database.ProductoDataBase
import com.example.proyectologin005d.data.local.JsonReader
import com.example.proyectologin005d.data.model.Producto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProductoRepository(private val context: Context) {

    private val productoDao = ProductoDataBase.getDatabase(context).productoDao()

    // Inicializar la base de datos con los datos del JSON si está vacía
    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (productoDao.obtenerPorId(1) == null) {
                cargarDatosIniciales()
            }
        }
    }

    private suspend fun cargarDatosIniciales() {
        val catalogoJson = JsonReader.cargarCatalogo(context)
        val productosEntidad = catalogoJson.map { Producto.fromJson(it) }
        productoDao.insertarTodos(productosEntidad)
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
}
