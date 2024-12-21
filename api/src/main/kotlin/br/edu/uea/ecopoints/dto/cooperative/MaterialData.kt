package br.edu.uea.ecopoints.dto.cooperative

import br.edu.uea.ecopoints.enums.material.MaterialType

data class MaterialData(
    val type: MaterialType,
    val totalQuantity: Double
)
