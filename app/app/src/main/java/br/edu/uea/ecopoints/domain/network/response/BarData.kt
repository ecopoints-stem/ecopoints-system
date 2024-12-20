package br.edu.uea.ecopoints.domain.network.response

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class BarData(
    @JsonProperty("totalsEmployees") val totalsEmployees: Int,
    @JsonProperty("cooperativeName") val cooperativeName: String,
    @JsonProperty("startDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") val startDate: LocalDate,
    @JsonProperty("endDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") val endDate: LocalDate,
    @JsonProperty("endDate") val data: Map<MaterialType, Double>
)
