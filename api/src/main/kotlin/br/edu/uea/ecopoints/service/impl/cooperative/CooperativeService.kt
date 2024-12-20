package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.cooperative.CooperativeRepository
import br.edu.uea.ecopoints.repository.cooperative.material.SeparatedMaterialRepository
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CooperativeService (
    private val cooperativeRepository: CooperativeRepository,
    private val separatedMaterialRepository: SeparatedMaterialRepository
) : ICooperativeService {
    @Transactional
    override fun save(cooperative: Cooperative): Cooperative = cooperativeRepository.save(cooperative)

    override fun findById(id: Long): Cooperative = cooperativeRepository.findById(id).orElseThrow{
        throw DomainException("Cooperativa com id $id não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun findByIdWithAdminEmployeesAndMaterials(id: Long): Cooperative = cooperativeRepository.findByIdWithAdminEmployeesAndMaterials(id).orElseThrow{
        throw DomainException("Cooperativa com id $id não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun findAllByEmployeeIdAndSeparatedDateBetween(
        employeeId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<SeparatedMaterial> = separatedMaterialRepository.findAllByEmployeeIdAndSeparatedDateBetween(employeeId, startDate, endDate)

    override fun findByCnpj(cnpj: String): Cooperative = cooperativeRepository.findByCnpj(cnpj) ?: throw DomainException("Cooperativa com cnpj $cnpj não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    override fun findByCnpjWithEmployees(cnpj: String): Cooperative = cooperativeRepository.findByCnpjWithEmployees(cnpj) ?: throw DomainException("Cooperativa com cnpj $cnpj não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    override fun findByCnpjWithAdministrator(cnpj: String): Cooperative = cooperativeRepository.findByCnpjWithAdministrator(cnpj).orElseThrow {
        throw DomainException("Cooperativa com cnpj $cnpj não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    }
    override fun existsById(id: Long): Boolean = cooperativeRepository.existsById(id)

    override fun existsByCpnj(cpnj: String): Boolean = cooperativeRepository.existsByCnpj(cpnj)

    override fun deleteById(id: Long) {
        cooperativeRepository.deleteById(id)
    }

}