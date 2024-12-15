package br.edu.uea.ecopoints.service.interf.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative

interface IReportService {
    fun generateAdminReport(cooperative: Cooperative) : ByteArray
}