package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RecyclingSorterRegister(
    @field:NotBlank val name: String,
    @field:Nullable @field:Size(max = 13) val phone: String?,
    @field:Email val email: String,
    @field:NotBlank @field:Size(min = 7,
        message = "Senha não pode ser menor que 7 caracteres"
    ) val password: String,
    @field:NotBlank @field:Size(max = 11,
        message = "CPF deve ter , no máximo, 11 caracteres"
    ) val cpf: String,
    @field:Nullable val cnpj: String? = null,
) {
    fun toEntity() = RecyclingSorter(
        name = this.name,
        phone = this.phone,
        email = this.email,
        password = this.password,
        cpf = this.cpf
    )
}
