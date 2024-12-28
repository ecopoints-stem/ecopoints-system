package br.edu.uea.ecopoints.view.pickup

import br.edu.uea.ecopoints.enums.PickupRequestStatus
import br.edu.uea.ecopoints.enums.material.MaterialType
import java.math.BigDecimal
import java.time.LocalDate


data class RecyclingPickupRequestView(
    val id: Long,
    val materialType: MaterialType,
    val quantity: Double,
    val unitPrice: BigDecimal,
    val address: String,
    val requestDate: LocalDate,
    val status: PickupRequestStatus,
    val driverId: Long?,
    val requesterId: Long,
    val clientId: Long
)
