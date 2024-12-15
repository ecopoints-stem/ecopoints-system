package br.edu.uea.ecopoints.dto.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SeparatedMaterialRegister(
    @field:NotNull val separatedDate: String,
    @field:NotNull val materialId: Long,
    @field:NotNull @field:Positive val quantity: Double
) {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
    fun toEntity() = SeparatedMaterial(
        separatedDate = LocalDateTime.parse(separatedDate, formatter),
        quantity = quantity
    )
}