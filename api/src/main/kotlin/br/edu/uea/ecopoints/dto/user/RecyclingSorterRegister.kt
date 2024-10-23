package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import org.hibernate.validator.constraints.br.CNPJ
import org.hibernate.validator.constraints.br.CPF

data class RecyclingSorterRegister(
    @field:NotBlank val name: String,
    @field:Nullable @field:Size(max = 13, message = "O telefone tem, no máximo 13 caracteres") val phone: String?,
    @field:Email(message = "Email deve ser válido") val email: String,
    @field:NotBlank @field:Size(min = 7,
        message = "Senha não pode ser menor que 7 caracteres"
    ) val password: String,
    @field:NotBlank @field:Size(max = 11,
        message = "CPF deve ter , no máximo, 11 caracteres"
    ) @CPF val cpf: String,
    @field:Nullable @CNPJ val cnpj: String? = null,
) {
    fun toEntity() = RecyclingSorter(
        name = this.name,
        phone = this.phone,
        email = this.email,
        password = this.password,
        cpf = this.cpf
    )
}
