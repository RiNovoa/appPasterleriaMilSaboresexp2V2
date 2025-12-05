package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectologin005d.data.dao.ProductoDao
import com.example.proyectologin005d.data.dao.ResenaDao
import com.example.proyectologin005d.data.model.Producto
import com.example.proyectologin005d.data.model.Resena

@Database(entities = [Producto::class, Resena::class], version = 2, exportSchema = false)
abstract class ProductoDataBase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun resenaDao(): ResenaDao

    companion object {
        @Volatile
        private var INSTANCE: ProductoDataBase? = null

        fun getDatabase(context: Context): ProductoDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductoDataBase::class.java,
                    "pasteleria_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
