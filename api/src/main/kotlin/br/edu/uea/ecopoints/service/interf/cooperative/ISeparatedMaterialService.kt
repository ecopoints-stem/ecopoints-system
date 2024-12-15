package br.edu.uea.ecopoints.service.interf.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import br.edu.uea.ecopoints.enums.material.MaterialType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ISeparatedMaterialService {
    fun save(separatedMaterial: SeparatedMaterial) : SeparatedMaterial
    fun findById(id: Long) : SeparatedMaterial
    fun findAllByEmployeeId(employeeId: Long, pageable: Pageable) : Page<SeparatedMaterial>
    fun findAllByEmployeeIdAndMaterialType(employeeId: Long, materialType: MaterialType) : List<SeparatedMaterial>
    fun deleteById(id: Long)
}