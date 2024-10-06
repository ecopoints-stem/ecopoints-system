package br.edu.uea.ecopoints.domain

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.domain.user.Driver
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import br.edu.uea.ecopoints.enums.PickupRequestStatus.IN_PROGRESS
import br.edu.uea.ecopoints.enums.material.MaterialType
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
    @Column(nullable = false)
    val unitPrice: BigDecimal,
    @Column(nullable = false)
    val pDate: LocalDate,
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    var status: PickupRequestStatus = IN_PROGRESS,
    @ManyToOne(optional = true) @JoinColumn(name = "driver_id", nullable = true)
    var driver: Driver? = null,
    @ManyToOne(optional = false) @JoinColumn(name = "requester_id", nullable = false)
    val requester: CooperativeAdministrator
)