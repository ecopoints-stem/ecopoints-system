package br.edu.uea.ecopoints.domain.entity

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDate

data class PickUpRequest(
    @JsonProperty("id") val id: Long,
    @JsonProperty("materialType") val materialType: MaterialType,
    @JsonProperty("quantity") val quantity: Double,
    @JsonProperty("unitPrice") val unitPrice: BigDecimal,
    @JsonProperty("address") val address: String,
    @JsonProperty("requestDate") val requestDate: LocalDate,
    @JsonProperty("status") val status: PickupRequestStatus,
    @JsonProperty("driverId") val driverId: Long?,
    @JsonProperty("requesterId") val requesterId: Long,
    @JsonProperty("coopAdminId") val coopAdminId: Long
)
