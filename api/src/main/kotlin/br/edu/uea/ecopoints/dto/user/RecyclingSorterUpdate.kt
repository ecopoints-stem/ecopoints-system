package br.edu.uea.ecopoints.dto.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class RecyclingSorterUpdate(
    @field:NotNull(message = "Nome obrigatório") val name: String,
    @field:Email(message = "Email inválido") val email: String,
    @field:Nullable val phone: String?,
    @field:Nullable val cpnjCooperative: String?,
    @field:NotNull val password: String
) {
    fun toEntity(employee: RecyclingSorter) = RecyclingSorter(
        id = employee.id!!,
        name = this.name,
        phone = this.phone,
        email = this.email,
        password = this.password,
        cpf = employee.cpf
    )
}