package com.example.proyectologin005d.data.model

data class Credential (val username:String,val password:String){
    // objeto para acceder a la instancia
    companion object{
    val Admin= Credential(username="admin", password = "123")
    }

}//fin data