package br.edu.uea.ecopoints.config.security.authentication.request

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequest(
    @field:NotBlank val refreshToken: String
) {
    constructor() : this("")
}
