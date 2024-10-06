package br.edu.uea.ecopoints.config.security.authentication.response

data class LoginResponse(
    val id: Long?,
    val role: String?,
    val accessToken: String,
    val refreshToken: String
)
