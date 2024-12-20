package br.edu.uea.ecopoints.repository.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import br.edu.uea.ecopoints.enums.material.MaterialType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface SeparatedMaterialRepository : JpaRepository<SeparatedMaterial, Long> {
    fun findAllByEmployee_IdOrderBySeparatedDateDesc(employeeId: Long, pageable: Pageable) : Page<SeparatedMaterial>

    @Query("""
        SELECT sm 
        FROM SeparatedMaterial sm 
        WHERE sm.employee.id = :employeeId 
          AND sm.separatedDate BETWEEN :startDate AND :endDate
        ORDER BY sm.separatedDate DESC
    """)
    fun findAllByEmployeeIdAndSeparatedDateBetween(
        employeeId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): List<SeparatedMaterial>

    @Query("SELECT sm FROM SeparatedMaterial sm WHERE sm.typeOfMaterial.type = :materialType AND sm.employee.id = :employeeId ORDER BY sm.separatedDate DESC")
    fun findAllByTypeOfMaterialAnEmployeeIdOrderedBySeparatedDate(materialType: MaterialType, employeeId: Long): List<SeparatedMaterial>
}