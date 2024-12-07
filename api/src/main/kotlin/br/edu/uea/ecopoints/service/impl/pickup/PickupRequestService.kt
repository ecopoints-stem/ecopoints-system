package br.edu.uea.ecopoints.service.impl.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.pickup.RecPickupRequestRepository
import br.edu.uea.ecopoints.service.interf.pickup.IPickupRequestService
import org.springframework.stereotype.Service

@Service
class PickupRequestService (
    private val repo: RecPickupRequestRepository
): IPickupRequestService {
    override fun save(pick: RecyclingPickupRequest): RecyclingPickupRequest = repo.save(pick)

    override fun findById(id: Long): RecyclingPickupRequest = repo.findById(id).orElseThrow{
        throw DomainException(message = "Pedido de requisição com id $id não encontrado", type = ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun findByIdWithDriverAndRequester(id: Long): RecyclingPickupRequest = repo.findByIdWithDriverAndRequester(id).orElseThrow{
        throw DomainException(message = "Pedido de requisição com id $id não encontrado", type = ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun existsById(id: Long): Boolean = repo.existsById(id)

    override fun deleteById(id: Long) {
        repo.deleteById(id)
    }

}