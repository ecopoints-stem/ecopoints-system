package br.edu.uea.ecopoints.config.security.authentication.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:Email(message = "Email deve ser válido") val email: String,
    @field:NotBlank(message = "Senha não pode ser vazia")
    @field:Size(min = 7,
        message = "Senha não pode ser menor que 7 caracteres"
    ) val password: String
)
