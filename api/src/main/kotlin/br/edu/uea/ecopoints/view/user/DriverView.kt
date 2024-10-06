package br.edu.uea.ecopoints.view.user

data class DriverView(
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String,
    val cnh: String
)
