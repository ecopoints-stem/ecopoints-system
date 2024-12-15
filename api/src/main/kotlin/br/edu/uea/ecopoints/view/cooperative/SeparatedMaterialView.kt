package br.edu.uea.ecopoints.view.cooperative

import br.edu.uea.ecopoints.enums.material.MaterialType
import java.time.LocalDateTime

data class SeparatedMaterialView(
    val id: Long,
    val separatedDate: LocalDateTime,
    val employeeId: Long,
    val materialId: Long,
    val materialType: MaterialType,
    val materialName: String,
    val quantity: Double
)
