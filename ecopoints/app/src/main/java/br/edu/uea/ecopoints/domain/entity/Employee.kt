package br.edu.uea.ecopoints.domain.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class Employee(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("cpf") val cpf: String,
    @JsonProperty("cooperativeId") val cooperativeId: Long?
)
