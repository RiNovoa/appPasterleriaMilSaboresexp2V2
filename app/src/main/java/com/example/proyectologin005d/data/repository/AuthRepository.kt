package com.example.proyectologin005d.data.repository

import android.content.Context
import com.example.proyectologin005d.data.database.SessionDataStore
import com.example.proyectologin005d.data.model.User
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

class AuthRepository(private val context: Context) {

    private val session = SessionDataStore(context)
    private val usersFile by lazy {
        val databaseDir = File(context.filesDir, "database")
        if (!databaseDir.exists()) {
            databaseDir.mkdirs()
        }
        File(databaseDir, "Usuarios.json")
    }

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    init {
        initializeUsersFile()
    }

    private fun initializeUsersFile() {
        if (!usersFile.exists()) {
            try {
                context.assets.open("database/Usuarios.json").use { inputStream ->
                    usersFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun readUsers(): MutableList<User> {
        return if (usersFile.exists() && usersFile.readText().isNotBlank()) {
            try {
                json.decodeFromString<MutableList<User>>(usersFile.readText())
            } catch (e: Exception) {
                e.printStackTrace()
                mutableListOf()
            }
        } else {
            mutableListOf()
        }
    }

    private fun writeUsers(users: List<User>) {
        try {
            usersFile.writeText(json.encodeToString(users))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun register(nombre: String, apellido: String, correo: String, contrasena: String, fechaNacimiento: String, direccion: String): Boolean {
        val users = readUsers()
        if (users.any { it.correo.equals(correo, ignoreCase = true) }) {
            return false
        }
        val newId = (users.maxOfOrNull { it.id } ?: 0) + 1
        val newUser = User(
            id = newId,
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            contrasena = contrasena,
            role = "user",
            fechaNacimiento = fechaNacimiento,
            direccion = direccion
        )
        users.add(newUser)
        writeUsers(users)
        session.saveUser(newUser.correo)
        return true
    }

    suspend fun login(correo: String, contrasena: String): Boolean {
        val users = readUsers()
        val user = users.find { it.correo.equals(correo, ignoreCase = true) && it.contrasena == contrasena }
        return if (user != null) {
            session.saveUser(user.correo)
            true
        } else {
            false
        }
    }

    suspend fun getSessionUsername(): String? = session.currentUser.first()

    fun getUserByUsername(correo: String): User? {
        val users = readUsers()
        return users.find { it.correo.equals(correo, ignoreCase = true) }
    }

    suspend fun logout(): Unit = session.clearUser()
}