package br.edu.uea.ecopoints.domain.network.request

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import com.fasterxml.jackson.annotation.JsonProperty

data class MaterialRegister(
    @JsonProperty("name") val name: String,
    @JsonProperty("type") val type: MaterialType
)
