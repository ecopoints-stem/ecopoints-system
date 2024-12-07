package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import br.edu.uea.ecopoints.repository.cooperative.AttendanceRecordRepository
import br.edu.uea.ecopoints.service.interf.cooperative.IAttendanceRecordService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AttendanceRecordService(
    private val attendanceRecordRepository: AttendanceRecordRepository
) : IAttendanceRecordService {
    @Transactional
    override fun save(at: AttendanceRecord): AttendanceRecord = attendanceRecordRepository.save(at)

    override fun findLastByEmployeeId(employeeId: Long): AttendanceRecord? = attendanceRecordRepository.findTopByRecyclingSorterIdOrderByPDateDescEntryTimeDesc(employeeId)
    override fun findByRecyclingSorterIdAndPDate(recyclingSorterId: Long, pDate: LocalDate): AttendanceRecord? = attendanceRecordRepository.findByRecyclingSorterIdAndPDate(recyclingSorterId,pDate)

}