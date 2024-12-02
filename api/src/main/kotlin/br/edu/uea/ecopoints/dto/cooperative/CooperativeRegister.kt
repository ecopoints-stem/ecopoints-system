package br.edu.uea.ecopoints.dto.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CNPJ

data class CooperativeRegister(
    @NotNull val name: String = "",
    @NotNull @CNPJ val cnpj: String = "",
    @Nullable var adminId: Long? = null,
    @NotNull var employeesId: List<Long> = mutableListOf(),
    @NotNull var materialsId: List<Long> = mutableListOf()
) {
    fun toEntity() : Cooperative = Cooperative(
        name = name,
        cnpj = cnpj
    )
}