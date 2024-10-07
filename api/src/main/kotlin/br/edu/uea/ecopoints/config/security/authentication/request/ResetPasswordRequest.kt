package br.edu.uea.ecopoints.config.security.authentication.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class ResetPasswordRequest(
    @field:NotBlank @field:Size(min = 7) val newPassword: String,
    @field:NotBlank @field:Size(min = 7) val temporaryPassword: String
)