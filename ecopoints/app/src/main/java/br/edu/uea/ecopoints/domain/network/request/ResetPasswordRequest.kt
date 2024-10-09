package br.edu.uea.ecopoints.domain.network.request

import com.fasterxml.jackson.annotation.JsonProperty

data class ResetPasswordRequest(
    @JsonProperty("newPassword") val newPassword: String,
    @JsonProperty("temporaryPassword") val temporaryPassword: String
)
