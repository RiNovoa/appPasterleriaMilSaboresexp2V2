package com.example.proyectologin005d.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel que mantiene el estado de los datos obtenidos
class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    // Flujo mutable que contiene la lista de posts
    private val _postList = MutableStateFlow<List<Post>>(emptyList())

    // Flujo público de solo lectura
    val postList: StateFlow<List<Post>> = _postList

    // Se llama automáticamente al iniciar
    init {
        fetchPosts()
    }

    // Función que obtiene los datos en segundo plano
    private fun fetchPosts() {
        viewModelScope.launch {
            try {
                _postList.value = repository.getPosts()
            } catch (e: Exception) {
                println("Error al obtener datos: ${e.localizedMessage}")
            }
        }
    }
}
