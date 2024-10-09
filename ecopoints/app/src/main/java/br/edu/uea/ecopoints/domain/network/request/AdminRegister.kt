package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AdminRegister(
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("password") val password: String,
    @JsonProperty("securityQuestion") val securityQuestion: String?,
    @JsonProperty("securityResponse") val securityResponse: String?,
    @JsonProperty("cooperativeName") val cooperativeName: String?,
    @JsonProperty("cooperativeCnpj") val cooperativeCnpj: String?
)
