package br.edu.uea.ecopoints.domain.entity

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import com.fasterxml.jackson.annotation.JsonProperty

data class Material(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: MaterialType
)
