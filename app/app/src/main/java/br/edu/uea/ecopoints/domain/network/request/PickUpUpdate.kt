package br.edu.uea.ecopoints.domain.network.request

import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class PickUpUpdate(
    @JsonProperty("id") val id: Long,
    @JsonProperty("status") val status: PickupRequestStatus,
    @JsonProperty("unitPrice") val unitPrice: BigDecimal,
    @JsonProperty("requestDate") val requestDate : LocalDate,
    @JsonProperty("driverId") val driverId: Long?
)
