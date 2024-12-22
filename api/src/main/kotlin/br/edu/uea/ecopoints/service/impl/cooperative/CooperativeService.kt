package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.enums.material.MaterialType
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.cooperative.CooperativeRepository
import br.edu.uea.ecopoints.repository.cooperative.material.SeparatedMaterialRepository
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.view.cooperative.BarData
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

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

    override fun findBarDataByCooperativeIdAndSeparatedDateBetween(
        cooperativeId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): BarData {
        val pairMap = separatedMaterialRepository.findMaterialQuantityByCooperativeIdAndSeparatedDateBetween(
            cooperativeId, startDate, endDate
        )
        val data = HashMap<MaterialType, Double>()
        pairMap.forEach {
            data[it.type] = it.totalQuantity/1000 // Transformando pra tonelada
        }
        val cooperativeName = cooperativeRepository.findById(cooperativeId).orElseThrow{
            throw DomainException(message = "Cooperativa com id $cooperativeId não encontrado", type = ExceptionDetailsStatus.INVALID_INPUT)
        }.name
        val totalEmployees = cooperativeRepository.countEmployeesByCooperativeId(cooperativeId)
        val barData = BarData(
            totalsEmployees = totalEmployees,
            cooperativeName = cooperativeName,
            startDate = startDate.toLocalDate(),
            endDate = endDate.toLocalDate(),
            data = data
        )
        return barData
    }

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