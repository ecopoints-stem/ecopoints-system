package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class DriverRegister(
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("cnh") val cnh: String,
    @JsonProperty("cpf") val cpf: String
)
