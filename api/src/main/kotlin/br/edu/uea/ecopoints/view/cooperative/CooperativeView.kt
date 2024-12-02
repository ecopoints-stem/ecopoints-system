package br.edu.uea.ecopoints.view.cooperative

data class CooperativeView(
    val id: Long,
    val name: String,
    val cnpj: String,
    val adminId: Long?,
    val employeesId : List<Long>,
    val materialsId : List<Long>
)