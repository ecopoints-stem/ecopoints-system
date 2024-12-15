package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.service.interf.cooperative.IReportService
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class ReportService : IReportService {
    override fun generateAdminReport(cooperative: Cooperative): ByteArray {
        val workbook = XSSFWorkbook()
        val materialsSheet = workbook.createSheet("materiais")
        createMaterialSheet(materialsSheet, cooperative, workbook)
        val cooperativeSheet = workbook.createSheet("cooperativa")
        createCooperativeSheet(cooperativeSheet, cooperative, workbook)
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return outputStream.toByteArray()
    }

    private fun createMaterialSheet(
        materialsSheet: org.apache.poi.ss.usermodel.Sheet,
        cooperative: Cooperative,
        workbook: XSSFWorkbook
    ) {
        val headerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            setFont(workbook.createFont().apply { bold = true })
        }
        val headerRow = materialsSheet.createRow(0)
        val headers = listOf("ID", "Tipo de Material", "Nome")
        headers.forEachIndexed { index, title ->
            val cell = headerRow.createCell(index, CellType.STRING)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }
        cooperative.materials.forEachIndexed { index, material ->
            val row = materialsSheet.createRow(index + 1)
            row.createCell(0,CellType.NUMERIC).setCellValue(material.id!!.toDouble())
            row.createCell(1,CellType.STRING).setCellValue(material.type.toString())
            row.createCell(2,CellType.STRING).setCellValue(material.name)
        }
        headers.indices.forEach { materialsSheet.autoSizeColumn(it) }
    }

    private fun createCooperativeSheet(
        cooperativeSheet: org.apache.poi.ss.usermodel.Sheet,
        cooperative: Cooperative,
        workbook: XSSFWorkbook
    ) {
        val headerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            setFont(workbook.createFont().apply { bold = true })
        }

        val headerRow = cooperativeSheet.createRow(0)
        val headers = listOf("ID", "Nome", "Email")
        headers.forEachIndexed { index, title ->
            val cell = headerRow.createCell(index, CellType.STRING)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }
        cooperative.employees.forEachIndexed { index, employee ->
            val row = cooperativeSheet.createRow(index + 1)
            row.createCell(0,CellType.NUMERIC).setCellValue(employee.id!!.toDouble())
            row.createCell(1,CellType.STRING).setCellValue(employee.name)
            row.createCell(2,CellType.STRING).setCellValue(employee.email)
        }
        headers.indices.forEach { cooperativeSheet.autoSizeColumn(it) }
    }
}