package br.edu.uea.ecopoints.dto.pickup

import br.edu.uea.ecopoints.enums.PickupRequestStatus
import jakarta.validation.constraints.NotNull

data class RecyclingPickupRequestConsultStatus(
    @field:NotNull val pickDate: String,
    @field:NotNull val statuses: List<PickupRequestStatus>,
)
