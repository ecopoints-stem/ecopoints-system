package br.edu.uea.ecopoints.service.interf.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import java.time.LocalDateTime

interface ICooperativeService {
    fun save(cooperative: Cooperative) : Cooperative
    fun findById(id: Long) : Cooperative
    fun findByIdWithAdminEmployeesAndMaterials(id: Long) : Cooperative
    fun findAllByEmployeeIdAndSeparatedDateBetween(employeeId: Long, startDate: LocalDateTime, endDate: LocalDateTime) : List<SeparatedMaterial>
    fun findByCnpj(cnpj: String) : Cooperative
    fun findByCnpjWithEmployees(cnpj: String) : Cooperative
    fun findByCnpjWithAdministrator(cnpj: String) : Cooperative
    fun existsById(id: Long) : Boolean
    fun existsByCpnj(cpnj: String) : Boolean
    fun deleteById(id: Long)
}