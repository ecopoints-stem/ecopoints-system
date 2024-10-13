package br.edu.uea.ecopoints.config.security.authentication.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank(message = "Token não pode ser nulo ou vazio") val refreshToken: String
) {
    constructor() : this("")
}
