package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.Driver
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class DriverRegister(
    @field:NotBlank val name: String,
    @field:Nullable @field:Size(max = 13) val phone: String?,
    @field:Email val email: String,
    @field:NotBlank @field:Size(min = 7,
        message = "Senha não pode ser menor que 7 caracteres"
    ) val password: String,
    @field:NotBlank @field:Size(max = 10) val cnh: String,
    @field:NotBlank @field:Size(max = 11) val cpf: String
) {
    fun toEntity() = Driver(
        name = this.name,
        phone = this.phone,
        email = this.email,
        password = this.password,
        cnh = this.cnh,
        cpf = this.cpf
    )
}