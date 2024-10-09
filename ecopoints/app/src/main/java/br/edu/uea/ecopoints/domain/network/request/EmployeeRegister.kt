package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class EmployeeRegister(
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("cpf") val cpf: String,
    @JsonProperty("cnpj") val cnpj: String?
)
