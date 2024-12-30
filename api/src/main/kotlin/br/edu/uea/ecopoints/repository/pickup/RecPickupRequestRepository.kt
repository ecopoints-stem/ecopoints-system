package br.edu.uea.ecopoints.repository.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*

@Repository
interface RecPickupRequestRepository : JpaRepository<RecyclingPickupRequest, Long> {
    @Query("""
        SELECT r 
        FROM RecyclingPickupRequest r 
        LEFT JOIN FETCH r.driver d 
        JOIN FETCH r.requester a 
        JOIN FETCH r.coopAdmin c
        WHERE r.id = :id
    """)
    fun findByIdWithDriverAndRequester(@Param("id") id: Long): Optional<RecyclingPickupRequest>
    fun findAllByPickDateAndDriver_Id(pickDate: LocalDate, driverId: Long, pageable: Pageable): Page<RecyclingPickupRequest>
    fun findAllByRequester_IdOrderByPickDateDesc(requesterId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByCoopAdmin_IdOrderByPickDateDesc(cooperativeAdminId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriver_IdOrderByPickDateDesc(driverId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriver_IdAndStatusInAndPickDateOrderByPickDateDesc(driverId: Long, statuses: List<PickupRequestStatus>, pickDate: LocalDate, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriver_IdAndStatusInAndPickDateBetweenOrderByPickDateDesc(
        driverId: Long,
        statuses: List<PickupRequestStatus>,
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<RecyclingPickupRequest>
    fun findAllByDriver_IdAndStatusInOrderByPickDateDesc(
        driverId: Long,
        statuses: List<PickupRequestStatus>,
        pageable: Pageable
    ): Page<RecyclingPickupRequest>
    fun findAllByCoopAdmin_IdAndRequester_IdAndStatusInAndPickDateBetween(
        coopAdminId: Long, requesterId: Long,
        statuses: List<PickupRequestStatus>, startDate: LocalDate, endDate: LocalDate
    ) : List<RecyclingPickupRequest>
}