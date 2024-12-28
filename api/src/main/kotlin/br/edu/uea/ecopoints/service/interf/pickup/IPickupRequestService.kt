package br.edu.uea.ecopoints.service.interf.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface IPickupRequestService {
    fun save(pick: RecyclingPickupRequest) : RecyclingPickupRequest
    fun findById(id: Long) : RecyclingPickupRequest
    fun findByIdWithDriverAndRequester(id: Long) : RecyclingPickupRequest
    fun findAllByDateAndDriverId(pickDate: LocalDate, driverId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByRequesterId(requesterId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByClientId(cooperativeAdminId: Long , pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriverId(driverId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriverIdAndStatusInAndPickDate(driverId: Long, statuses: List<PickupRequestStatus>, pickDate: LocalDate, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriverIdAndStatusIn(driverId: Long, statuses: List<PickupRequestStatus>, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByRequesterIdAndStatusIn(requesterId: Long, clientId : Long, startDate: LocalDate, endDate: LocalDate, statuses: List<PickupRequestStatus>) : List<RecyclingPickupRequest>
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}