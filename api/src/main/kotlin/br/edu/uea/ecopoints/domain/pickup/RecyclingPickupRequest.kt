package br.edu.uea.ecopoints.domain.pickup

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.domain.user.Driver
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import br.edu.uea.ecopoints.enums.PickupRequestStatus.IN_PROGRESS
import br.edu.uea.ecopoints.enums.material.MaterialType
import br.edu.uea.ecopoints.view.pickup.RecyclingPickupRequestView
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
class RecyclingPickupRequest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    val materialType: MaterialType,
    @Column(nullable = false)
    val quantity: Double,
    @Column(nullable = false, scale = 7, precision = 2)
    val unitPrice: BigDecimal,
    @Column(nullable = false, length = 120)
    val address: String,
    @Column(nullable = false)
    val pDate: LocalDate,
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    var status: PickupRequestStatus = IN_PROGRESS,
    @ManyToOne(optional = true) @JoinColumn(name = "driver_id", nullable = true)
    var driver: Driver? = null,
    @ManyToOne(optional = false) @JoinColumn(name = "requester_id", nullable = false)
    val requester: CooperativeAdministrator
) {
    fun toView() : RecyclingPickupRequestView = RecyclingPickupRequestView(
        id = this.id!!,
        materialType = this.materialType,
        quantity = this.quantity,
        unitPrice = this.unitPrice,
        address = this.address,
        requestDate = this.pDate,
        status = this.status,
        driverId = this.driver?.id,
        requesterId = this.requester.id!!
    )
}