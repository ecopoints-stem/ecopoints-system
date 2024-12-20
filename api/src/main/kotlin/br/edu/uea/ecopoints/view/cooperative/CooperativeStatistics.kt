package br.edu.uea.ecopoints.view.cooperative

import br.edu.uea.ecopoints.enums.material.MaterialType

data class CooperativeStatistics(
    val cooperativeName: String,
    val numberOfEmployees: Int,
    val quantity: Map<MaterialType, Double> = emptyMap()
)