package br.edu.uea.ecopoints.view.user

data class CoopAdmView(
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String,
    val securityQuestion: String? = null
)
