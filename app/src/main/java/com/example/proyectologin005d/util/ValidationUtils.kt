package com.example.proyectologin005d.util

object ValidationUtils {

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun isValidCardNumber(number: String): Boolean {
        return number.length == 16 && number.all { it.isDigit() }
    }

    fun isValidExpiryDate(date: String): Boolean {
        if (date.length != 4 || !date.all { it.isDigit() }) return false
        val month = date.take(2).toIntOrNull() ?: return false
        return month in 1..12
    }

    fun isValidCvv(cvv: String): Boolean {
        return cvv.length in 3..4 && cvv.all { it.isDigit() }
    }
}
