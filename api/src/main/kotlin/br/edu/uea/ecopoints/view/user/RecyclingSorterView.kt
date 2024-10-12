package br.edu.uea.ecopoints.view.user

data class RecyclingSorterView(
    val id: Long,
    val name: String,
    val phone: String?,
    val email: String,
    val cpf: String,
    val cooperativeId: Long?
)
