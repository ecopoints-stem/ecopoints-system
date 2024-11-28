package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class AdminUpdate(
    @field:NotNull(message = "Nome obrigatório") val name: String,
    @field:Email(message = "Email inválido") val email: String,
    @field:Nullable val phone: String?,
    @field:NotNull(message = "Senha obrigatória") val password: String
) {
    fun toEntity(admin: CooperativeAdministrator) = CooperativeAdministrator(
        id = admin.id,
        name = name,
        phone = phone,
        email = email,
        password = admin.password,
        securityQuestion = admin.securityQuestion,
        securityResponse = admin.securityResponse,
    )
}