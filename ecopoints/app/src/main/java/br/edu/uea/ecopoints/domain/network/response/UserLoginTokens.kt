package br.edu.uea.ecopoints.domain.network.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UserLoginTokens(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("role") val role: String?,
    @JsonProperty("isPasswordRecovery") val isPasswordRecovery: Boolean,
    @JsonProperty("accessToken") val accessToken: String,
    @JsonProperty("refreshToken") val refreshToken: String
)
