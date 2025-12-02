package com.example.proyectologin005d.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.proyectologin005d.data.local.CatalogoProductoJson

@Entity(tableName = "productos")
data class Producto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val imagen: String?,
    val categoria: String?,
    val descripcion: String?
) {
    // Función de extensión para convertir CatalogoProductoJson a Producto
    companion object {
        fun fromJson(json: CatalogoProductoJson): Producto {
            return Producto(
                id = json.id,
                nombre = json.nombre,
                precio = json.precio,
                stock = json.stock,
                imagen = json.imagen,
                categoria = json.categoria,
                descripcion = json.descripcion
            )
        }
    }
    
    // Función para convertir a CatalogoProductoJson (si es necesario para la UI existente)
    fun toJson(): CatalogoProductoJson {
        return CatalogoProductoJson(
            id = id,
            nombre = nombre,
            precio = precio,
            stock = stock,
            imagen = imagen,
            categoria = categoria,
            descripcion = descripcion
        )
    }
}
