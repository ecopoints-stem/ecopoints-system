package br.edu.uea.ecopoints.service.impl.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.enums.PickupRequestStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.pickup.RecPickupRequestRepository
import br.edu.uea.ecopoints.service.interf.pickup.IPickupRequestService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate

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

    override fun findAllByDateAndDriverId(
        pickDate: LocalDate,
        driverId: Long,
        pageable: Pageable
    ): Page<RecyclingPickupRequest> = repo.findAllByPickDateAndDriver_Id(pickDate, driverId, pageable)

    override fun findAllByRequesterId(requesterId: Long, pageable: Pageable): Page<RecyclingPickupRequest> = repo.findAllByRequester_IdOrderByPickDateDesc(requesterId, pageable)
    override fun findAllByCoopAdminId(cooperativeAdminId: Long, pageable: Pageable): Page<RecyclingPickupRequest> = repo.findAllByCoopAdmin_IdOrderByPickDateDesc(cooperativeAdminId, pageable)

    override fun findAllByDriverId(driverId: Long, pageable: Pageable): Page<RecyclingPickupRequest> = repo.findAllByDriver_IdOrderByPickDateDesc(driverId, pageable)
    override fun findAllByDriverIdAndStatusInAndPickDate(
        driverId: Long,
        statuses: List<PickupRequestStatus>,
        pickDate: LocalDate,
        pageable: Pageable
    ): Page<RecyclingPickupRequest> = repo.findAllByDriver_IdAndStatusInAndPickDateOrderByPickDateDesc(driverId, statuses, pickDate, pageable)

    override fun findAllByDriverIdAndStatusIn(
        driverId: Long,
        statuses: List<PickupRequestStatus>,
        pageable: Pageable
    ): Page<RecyclingPickupRequest> = repo.findAllByDriver_IdAndStatusInOrderByPickDateDesc(driverId, statuses, pageable)

    override fun findAllByRequesterIdAndClientIdAndStatusIn(
        coopAdminId: Long,
        requesterId: Long,
        statuses: List<PickupRequestStatus>,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<RecyclingPickupRequest> = repo.findAllByCoopAdmin_IdAndRequester_IdAndStatusInAndPickDateBetween(coopAdminId, requesterId, statuses, startDate, endDate)

    override fun existsById(id: Long): Boolean = repo.existsById(id)

    override fun deleteById(id: Long) {
        repo.deleteById(id)
    }

}