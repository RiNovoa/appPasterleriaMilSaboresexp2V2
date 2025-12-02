package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.proyectologin005d.data.model.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos")
    fun obtenerTodos(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Producto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(producto: Producto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(productos: List<Producto>)

    @Update
    suspend fun actualizar(producto: Producto)

    @Delete
    suspend fun eliminar(producto: Producto)
    
    @Query("DELETE FROM productos")
    suspend fun eliminarTodos()
}
