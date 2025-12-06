package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.Resena
import kotlinx.coroutines.flow.Flow

@Dao
interface ResenaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResena(resena: Resena)

    @Query("SELECT * FROM resenas WHERE productId = :productId")
    fun getResenasByProduct(productId: Int): Flow<List<Resena>>
}
