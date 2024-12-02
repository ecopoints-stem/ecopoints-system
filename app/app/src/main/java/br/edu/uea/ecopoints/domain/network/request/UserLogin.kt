package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class UserLogin(
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String
)
