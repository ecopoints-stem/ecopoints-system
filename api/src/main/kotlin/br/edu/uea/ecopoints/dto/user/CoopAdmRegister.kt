package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CoopAdmRegister(
    @field:NotBlank val name: String,
    @field:Nullable @field:Size(max = 13) val phone: String?,
    @field:Email val email: String,
    @field:NotBlank @field:Size(min = 7,
        message = "Senha não pode ser menor que 7 caracteres"
    ) val password: String,
    @field:Nullable val securityQuestion: String? = null,
    @field:Nullable @field:Size(max = 60) val securityResponse: String? = null
) {
    fun toEntity() = CooperativeAdministrator(
        name = this.name,
        phone = this.phone,
        email = this.email,
        password = this.password,
        securityQuestion = this.securityQuestion,
        securityResponse = this.securityResponse
    )
}