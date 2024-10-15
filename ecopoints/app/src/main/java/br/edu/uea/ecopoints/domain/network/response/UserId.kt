package br.edu.uea.ecopoints.domain.network.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserId(
    @JsonProperty("id") val userId: Long?
)
