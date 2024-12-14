package br.edu.uea.ecopoints.service.interf.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface IPickupRequestService {
    fun save(pick: RecyclingPickupRequest) : RecyclingPickupRequest
    fun findById(id: Long) : RecyclingPickupRequest
    fun findByIdWithDriverAndRequester(id: Long) : RecyclingPickupRequest
    fun findAllByDateAndDriverId(pickDate: LocalDate, driverId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByRequesterId(requesterId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    fun findAllByDriverId(driverId: Long, pageable: Pageable) : Page<RecyclingPickupRequest>
    /*fun findAllByDriverId(driverId: Long, page: Int, size: Int) : Page<RecyclingPickupRequest>
    fun findAllByRequesterId(requesterId: Long, page: Int, size: Int) : Page<RecyclingPickupRequest>
    fun findAllByDateAndDriverId(pkDate: LocalDate, driverId: Long, page: Int, size: Int) : Page<RecyclingPickupRequest>*/
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}