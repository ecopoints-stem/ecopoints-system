package br.edu.uea.ecopoints.view.cooperative

import br.edu.uea.ecopoints.enums.material.MaterialType
import java.time.LocalDate

data class BarData(
    val totalsEmployees: Int,
    val cooperativeName: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val data: Map<MaterialType, Double>
)