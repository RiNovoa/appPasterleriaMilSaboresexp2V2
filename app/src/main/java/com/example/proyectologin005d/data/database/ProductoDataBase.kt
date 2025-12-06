package com.example.proyectologin005d.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.proyectologin005d.data.dao.CartDao
import com.example.proyectologin005d.data.dao.ContactDao
import com.example.proyectologin005d.data.dao.OrderDao
import com.example.proyectologin005d.data.dao.ProductoDao
import com.example.proyectologin005d.data.dao.ResenaDao
import com.example.proyectologin005d.data.dao.UserDao
import com.example.proyectologin005d.data.model.CartItemEntity
import com.example.proyectologin005d.data.model.ContactMessage
import com.example.proyectologin005d.data.model.OrderEntity
import com.example.proyectologin005d.data.model.Producto
import com.example.proyectologin005d.data.model.Resena
import com.example.proyectologin005d.data.model.User

@Database(
    entities = [
        Producto::class, 
        Resena::class, 
        User::class,
        OrderEntity::class,
        CartItemEntity::class,
        ContactMessage::class
    ], 
    version = 7, 
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ProductoDataBase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun resenaDao(): ResenaDao
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
    abstract fun cartDao(): CartDao
    abstract fun contactDao(): ContactDao

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
                //.allowMainThreadQueries() // Solo para pruebas, no en producción
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
