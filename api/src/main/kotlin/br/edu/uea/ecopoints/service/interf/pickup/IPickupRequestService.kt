package br.edu.uea.ecopoints.service.interf.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest

interface IPickupRequestService {
    fun save(pick: RecyclingPickupRequest) : RecyclingPickupRequest
    fun findById(id: Long) : RecyclingPickupRequest
    fun findByIdWithDriverAndRequester(id: Long) : RecyclingPickupRequest
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}