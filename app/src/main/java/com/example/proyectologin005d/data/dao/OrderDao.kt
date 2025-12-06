package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM ordenes")
    fun obtenerTodas(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(order: OrderEntity)
    
    @Query("SELECT * FROM ordenes ORDER BY id DESC LIMIT 1")
    fun obtenerUltimaOrden(): Flow<OrderEntity?>
}
