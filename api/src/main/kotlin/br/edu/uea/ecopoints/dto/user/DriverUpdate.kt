package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.Driver
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class DriverUpdate(
    @field:NotBlank val name: String,
    @field:Nullable @field:Size(max = 13, message = "O telefone tem, no máximo 13 caracteres") val phone: String?,
    @field:Email(message = "O email deve ser válido") val email: String,
    @field:NotBlank @field:Size(min = 7,
        message = "Senha não pode ser menor que 7 caracteres"
    ) val password: String,
    @field:NotBlank @field:Size(max = 10, message = "Máximo de 10 caracteres") val cnh: String
) {
    fun toEntity(driver: Driver) = Driver(
        id = driver.id!!,
        name = this.name,
        phone = this.phone,
        email = this.email,
        password = this.password,
        cnh = this.cnh,
        cpf = driver.cpf
    )
}
