package br.edu.uea.ecopoints.service.cooperative

import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import java.time.LocalDate

interface IAttendanceRecordService {
    fun save(at: AttendanceRecord) : AttendanceRecord
    fun findLastByEmployeeId(employeeId: Long) : AttendanceRecord?
    fun findByRecyclingSorterIdAndPDate(recyclingSorterId: Long, pDate: LocalDate) : AttendanceRecord?
}