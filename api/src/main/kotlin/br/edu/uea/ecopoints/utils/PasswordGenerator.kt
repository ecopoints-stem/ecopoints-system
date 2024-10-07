package br.edu.uea.ecopoints.utils

class PasswordGenerator {
    companion object {
        fun generateNewPassword() : String{
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..8).map { allowedChars.random() }.joinToString("")
        }
    }
}