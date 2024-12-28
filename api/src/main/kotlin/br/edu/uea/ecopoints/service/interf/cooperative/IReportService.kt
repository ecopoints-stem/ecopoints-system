package br.edu.uea.ecopoints.service.interf.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import java.time.LocalDate
import java.time.LocalDateTime

interface IReportService {
    fun generateAdminReport(cooperativeId: Long, startDate: LocalDateTime, endDate: LocalDateTime) : ByteArray
    fun generateClientReport(id : Long, clientId : Long, entityStartDate : LocalDate, entityEndDate : LocalDate) : ByteArray
}