package br.edu.uea.ecopoints.service.impl.user

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.repository.user.CoopAdmRepository
import br.edu.uea.ecopoints.service.user.ICoopAdmService
import org.springframework.stereotype.Service

@Service
class CoopAdmService (
    private val admRepository: CoopAdmRepository
) : ICoopAdmService {
    override fun save(driver: CooperativeAdministrator): CooperativeAdministrator = admRepository.save(driver)

    override fun findById(id: Long): CooperativeAdministrator = admRepository.findById(id).orElseThrow {
        throw RuntimeException("Não encontrado")
    }

    override fun existsById(id: Long): Boolean = admRepository.existsById(id)

    override fun deleteById(id: Long) {
        admRepository.deleteById(id)
    }

}