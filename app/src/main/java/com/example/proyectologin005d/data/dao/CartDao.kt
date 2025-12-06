package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM carrito")
    fun obtenerCarrito(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregar(item: CartItemEntity)

    @Query("DELETE FROM carrito WHERE productoId = :productoId")
    suspend fun eliminarProducto(productoId: Int)

    @Query("DELETE FROM carrito")
    suspend fun vaciar()
}
