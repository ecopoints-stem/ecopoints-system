package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SeparatedMaterialRegister(
    @JsonProperty("separatedDate") val separatedDate: String,
    @JsonProperty("materialId") val materialId: Long,
    @JsonProperty("quantity") val quantity: Double
)
