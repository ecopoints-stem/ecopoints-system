package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AdminUpdate(
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("password") val password: String
)
