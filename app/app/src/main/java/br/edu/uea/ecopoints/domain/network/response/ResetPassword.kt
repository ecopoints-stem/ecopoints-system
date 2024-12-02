package br.edu.uea.ecopoints.domain.network.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ResetPassword(
    @JsonProperty("message") val message: String
)
