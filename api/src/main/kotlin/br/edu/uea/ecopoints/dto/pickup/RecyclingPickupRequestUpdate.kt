package br.edu.uea.ecopoints.dto.pickup

import br.edu.uea.ecopoints.enums.PickupRequestStatus
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.annotation.Nullable
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class RecyclingPickupRequestUpdate(
    @field:NotNull val id: Long,
    @field:NotNull val status: PickupRequestStatus,
    @field:NotNull @field:Digits(integer = 10, fraction = 3) val unitPrice: BigDecimal,
    @field:NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") val requestDate : LocalDate,
    @field:Nullable val driverId: Long?
)
