package br.edu.uea.ecopoints.service.interf.cooperative

import java.time.LocalDate
import java.time.LocalDateTime

interface IReportService {
    fun generateAdminReport(cooperativeId: Long, startDate: LocalDateTime, endDate: LocalDateTime) : ByteArray
    fun generateCoopAdminReport(id : Long, coopAdminId : Long, entityStartDate : LocalDate, entityEndDate : LocalDate) : ByteArray
}