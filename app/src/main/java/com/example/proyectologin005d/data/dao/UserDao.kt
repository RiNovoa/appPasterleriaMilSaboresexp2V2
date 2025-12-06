package com.example.proyectologin005d.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectologin005d.data.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM usuarios WHERE correo = :email LIMIT 1")
    suspend fun obtenerPorCorreo(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(user: User)
    
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): User?
}
