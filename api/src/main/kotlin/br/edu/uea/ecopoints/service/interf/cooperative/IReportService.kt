package br.edu.uea.ecopoints.service.interf.cooperative

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator

interface IReportService {
    fun generateAdminReport(administrator: CooperativeAdministrator) : ByteArray
}