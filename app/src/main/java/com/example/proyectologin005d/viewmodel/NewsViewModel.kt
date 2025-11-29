package com.example.proyectologin005d.viewmodel

import android.util.Log // <--- 1. Agrega este import
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectologin005d.data.model.Post
import com.example.proyectologin005d.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {

    private val repository = NewsRepository()

    private val _postList = MutableStateFlow<List<Post>>(emptyList())
    val postList: StateFlow<List<Post>> = _postList

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch { // [cite: 21]
            try {
                _postList.value = repository.getPosts() // [cite: 21]
            } catch (e: Exception) {
                // 2. CAMBIO AQUÍ: Usamos Log.e para que el error se vea ROJO en Logcat
                Log.e("NewsViewModel", "Error al obtener datos: ${e.localizedMessage}")
            }
        }
    }
}