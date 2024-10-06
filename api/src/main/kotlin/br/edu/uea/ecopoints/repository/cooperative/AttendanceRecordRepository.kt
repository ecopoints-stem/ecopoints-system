package br.edu.uea.ecopoints.repository.cooperative

import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttendanceRecordRepository : JpaRepository<AttendanceRecord, Long> {

}