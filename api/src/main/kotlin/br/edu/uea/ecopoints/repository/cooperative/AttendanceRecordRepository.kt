package br.edu.uea.ecopoints.repository.cooperative

import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface AttendanceRecordRepository : JpaRepository<AttendanceRecord, Long> {
    fun findTopByRecyclingSorterIdOrderByPDateDescEntryTimeDesc(recyclingSorterId: Long): AttendanceRecord?
    fun findByRecyclingSorterIdAndPDate(recyclingSorterId: Long, pDate: LocalDate) : AttendanceRecord?
}