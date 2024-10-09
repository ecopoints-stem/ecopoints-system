package br.edu.uea.ecopoints.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Employee(
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("cpf") val cpf: String,
    @JsonProperty("cooperativeId") val cooperativeId: Long?
)
