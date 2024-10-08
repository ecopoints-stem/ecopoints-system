package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.cooperative.CooperativeRepository
import br.edu.uea.ecopoints.service.cooperative.ICooperativeService
import org.springframework.stereotype.Service

@Service
class CooperativeService (
    private val cooperativeRepository: CooperativeRepository
) : ICooperativeService {
    override fun save(cooperative: Cooperative): Cooperative = cooperativeRepository.save(cooperative)

    override fun findById(id: Long): Cooperative = cooperativeRepository.findById(id).orElseThrow{
        throw DomainException("Cooperativa com id $id não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun findByCnpj(cnpj: String): Cooperative = cooperativeRepository.findByCnpj(cnpj) ?: throw DomainException("Cooperativa com cnpj $cnpj não encontrado", ExceptionDetailsStatus.INVALID_INPUT)

    override fun existsById(id: Long): Boolean = cooperativeRepository.existsById(id)

    override fun existsByCpnj(cpnj: String): Boolean = cooperativeRepository.existsByCnpj(cpnj)

    override fun deleteById(id: Long) {
        cooperativeRepository.deleteById(id)
    }

}