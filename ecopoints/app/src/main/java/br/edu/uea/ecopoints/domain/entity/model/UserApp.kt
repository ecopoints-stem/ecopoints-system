package br.edu.uea.ecopoints.domain.entity.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserApp(
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val name: String,
    @JsonProperty("phone") val phone: String?,
    @JsonProperty("email") val email: String,
    @JsonProperty("isPasswordRecovery") val isPasswordRecovery: Boolean,
    @JsonProperty("role") val role: String
)
