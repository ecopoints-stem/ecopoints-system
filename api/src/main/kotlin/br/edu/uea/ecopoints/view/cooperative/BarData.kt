package br.edu.uea.ecopoints.view.cooperative

import br.edu.uea.ecopoints.enums.material.MaterialType
import java.time.LocalDate

data class BarData(
    val totalsEmployees: Long = 0,
    val cooperativeName: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val data: Map<MaterialType, Double> = emptyMap()
)