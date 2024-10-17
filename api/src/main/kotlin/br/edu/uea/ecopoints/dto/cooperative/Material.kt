package br.edu.uea.ecopoints.dto.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.enums.material.MaterialType
import jakarta.validation.constraints.NotNull

data class Material(
    @field:NotNull(message = "Campo nome deve ser válido") val name: String,
    @field:NotNull(message = "Campo MaterialType deve ser válido") val type: MaterialType
) {
    fun toEntity() = TypeOfMaterial(
        id = null,
        name = name,
        type = type
    )
}