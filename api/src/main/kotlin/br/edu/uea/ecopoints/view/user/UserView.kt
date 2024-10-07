package br.edu.uea.ecopoints.view.user

data class UserView(
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String,
    val isPasswordRecovery: Boolean,
    val role: String
)
