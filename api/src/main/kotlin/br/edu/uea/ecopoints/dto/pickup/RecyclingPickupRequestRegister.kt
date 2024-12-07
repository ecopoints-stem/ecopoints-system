package br.edu.uea.ecopoints.dto.pickup

import br.edu.uea.ecopoints.enums.material.MaterialType
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.annotation.Nonnull
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CNPJ
import java.math.BigDecimal
import java.time.LocalDate

data class RecyclingPickupRequestRegister(
    @field:CNPJ val cnpj: String,
    @field:Email val emailDriver: String,
    @field:NotNull val address: String,
    @field:NotNull val materialType: MaterialType,
    @field:NotNull val quantity: Double,
    @field:NotNull @field:Digits(integer = 10, fraction = 3) val unitPrice: BigDecimal,
    @field:NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") val requestDate : LocalDate
)
