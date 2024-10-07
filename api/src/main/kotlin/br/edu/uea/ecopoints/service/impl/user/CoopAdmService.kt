package br.edu.uea.ecopoints.service.impl.user

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.user.CoopAdmRepository
import br.edu.uea.ecopoints.service.user.ICoopAdmService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CoopAdmService (
    private val admRepository: CoopAdmRepository
) : ICoopAdmService {
    @Transactional
    override fun save(driver: CooperativeAdministrator): CooperativeAdministrator = admRepository.save(driver)

    override fun findById(id: Long): CooperativeAdministrator = admRepository.findById(id).orElseThrow {
        throw DomainException("Usuário do tipo CooperativeAdministrator com id $id não encontrado",ExceptionDetailsStatus.USER_NOT_FOUND)
    }

    override fun existsById(id: Long): Boolean = admRepository.existsById(id)

    override fun deleteById(id: Long) {
        admRepository.deleteById(id)
    }

}