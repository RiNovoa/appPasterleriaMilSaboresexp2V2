package com.example.proyectologin005d.data.repository

import android.content.Context
import com.example.proyectologin005d.data.database.ProductoDataBase
import com.example.proyectologin005d.data.database.SessionDataStore
import com.example.proyectologin005d.data.model.User
import kotlinx.coroutines.flow.first

class AuthRepository(private val context: Context) {

    private val session = SessionDataStore(context)
    private val userDao = ProductoDataBase.getDatabase(context).userDao()

    suspend fun register(nombre: String, apellido: String, correo: String, contrasena: String, fechaNacimiento: String, direccion: String): Boolean {
        val existingUser = userDao.obtenerPorCorreo(correo)
        if (existingUser != null) {
            return false
        }
        
        val newUser = User(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            contrasena = contrasena,
            role = "user",
            fechaNacimiento = fechaNacimiento,
            direccion = direccion
        )
        
        userDao.insertar(newUser)
        session.saveUser(newUser.correo)
        return true
    }

    suspend fun login(correo: String, contrasena: String): Boolean {
        val user = userDao.obtenerPorCorreo(correo)
        return if (user != null && user.contrasena == contrasena) {
            session.saveUser(user.correo)
            true
        } else {
            false
        }
    }

    suspend fun getSessionUsername(): String? = session.currentUser.first()

    suspend fun getUserByUsername(correo: String): User? {
        return userDao.obtenerPorCorreo(correo)
    }

    suspend fun logout(): Unit = session.clearUser()
}
