package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class EmployeeUpdate(
    @JsonProperty("name") val name: String,
    @JsonProperty("email") val email: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("cnpjCooperative") val cnpjCooperative: String?,
    @JsonProperty("password") val password: String
)
