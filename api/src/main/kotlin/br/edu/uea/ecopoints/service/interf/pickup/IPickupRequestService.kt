package br.edu.uea.ecopoints.service.interf.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import org.springframework.data.domain.Page

interface IPickupRequestService {
    fun save(pick: RecyclingPickupRequest) : RecyclingPickupRequest
    fun findById(id: Long) : RecyclingPickupRequest
    fun findByIdWithDriverAndRequester(id: Long) : RecyclingPickupRequest
    fun findAllByDriverId(driverId: Long, page: Int, size: Int) : Page<RecyclingPickupRequest>
    fun findAllByRequesterId(requesterId: Long, page: Int, size: Int) : Page<RecyclingPickupRequest>
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}