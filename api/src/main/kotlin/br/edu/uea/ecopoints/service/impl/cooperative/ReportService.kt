package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.enums.PickupRequestStatus.COMPLETED
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.interf.cooperative.IReportService
import br.edu.uea.ecopoints.service.interf.pickup.IPickupRequestService
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ReportService (
    private val cooperativeService: ICooperativeService,
    private val pickupService: IPickupRequestService
): IReportService {
    override fun generateAdminReport(cooperativeId: Long, startDate: LocalDateTime, endDate: LocalDateTime): ByteArray {
        val cooperative = cooperativeService.findByIdWithAdminEmployeesAndMaterials(cooperativeId)
        val workbook = XSSFWorkbook()
        val materialsSheet = workbook.createSheet("materiais")
        createMaterialSheet(materialsSheet, cooperative, workbook)
        val cooperativeSheet = workbook.createSheet("cooperativa")
        createCooperativeSheet(cooperativeSheet, cooperative, workbook)
        val materialsSeparatedSheet = workbook.createSheet("quantitativo")
        val map = HashMap<String, Double>()
        for (employee in cooperative.employees){
            val listSeparated : List<SeparatedMaterial> = cooperativeService.findAllByEmployeeIdAndSeparatedDateBetween(employeeId = employee.id!!, startDate = startDate, endDate = endDate)
            listSeparated.forEach { separatedMaterial ->
                val materialName = separatedMaterial.typeOfMaterial?.name ?: return@forEach
                val currentQuantity = map[materialName] ?: 0.0
                map[materialName] = currentQuantity + separatedMaterial.quantity
            }
        }
        createSeparatedSheet(materialsSeparatedSheet, map, workbook)
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return outputStream.toByteArray()
    }

    override fun generateCoopAdminReport(
        id: Long,
        coopAdminId: Long,
        entityStartDate: LocalDate,
        entityEndDate: LocalDate
    ): ByteArray {
        val result = pickupService.findAllByRequesterIdAndClientIdAndStatusIn(
            requesterId = id,
            coopAdminId = coopAdminId,
            startDate = entityStartDate,
            endDate = entityEndDate,
            statuses =  listOf(COMPLETED)
        )
        println("\n\n===Data de início $entityStartDate Data de Fim $entityEndDate===\n\n")
        println(result)
        println("\n\n")
        val workbook = XSSFWorkbook()
        val collectedMaterialSheet = workbook.createSheet("coletados")
        createCollectedMaterial(collectedMaterialSheet,result,workbook)
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)
        workbook.close()
        return outputStream.toByteArray()
    }

    private fun createCollectedMaterial(
        materialsSheet: org.apache.poi.ss.usermodel.Sheet,
        result: List<RecyclingPickupRequest>,
        workbook: XSSFWorkbook) {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val headerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            setFont(workbook.createFont().apply { bold = true })
        }
        val headerRow = materialsSheet.createRow(0)
        val headers = listOf("Data", "Quantidade", "Tipo de Material")
        headers.forEachIndexed { index, title ->
            val cell = headerRow.createCell(index, CellType.STRING)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }
        var totalValue = 0.0
        result.forEachIndexed { index: Int, pickup: RecyclingPickupRequest ->
            totalValue+=pickup.quantity*pickup.unitPrice.toDouble()
            val row = materialsSheet.createRow(index + 1)
            row.createCell(0,CellType.STRING).setCellValue(pickup.pickDate.format(formatter))
            row.createCell(1,CellType.NUMERIC).setCellValue(pickup.quantity)
            row.createCell(2,CellType.STRING).setCellValue(pickup.materialType.toString())
        }

        val row = materialsSheet.createRow(result.size+1)
        row.createCell(0,CellType.STRING).setCellValue("Total (R$) :")
        row.createCell(1,CellType.NUMERIC).setCellValue(totalValue)
        headers.indices.forEach { materialsSheet.autoSizeColumn(it) }
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

    private fun createSeparatedSheet(
        separatedMaterialsSheet: org.apache.poi.ss.usermodel.Sheet,
        separatedMaterials: Map<String, Double>,
        workbook: XSSFWorkbook
    ) {
        val headerStyle = workbook.createCellStyle().apply {
            alignment = HorizontalAlignment.CENTER
            setFont(workbook.createFont().apply { bold = true })
        }
        val headerRow = separatedMaterialsSheet.createRow(0)
        val headers = listOf("Nome do Material", "Quantidade (Toneladas)")
        headers.forEachIndexed { index, title ->
            val cell = headerRow.createCell(index, CellType.STRING)
            cell.setCellValue(title)
            cell.cellStyle = headerStyle
        }
        var count = 0
        separatedMaterials.forEach { (materialName: String, quantity: Double) ->
            val row = separatedMaterialsSheet.createRow(count + 1)
            row.createCell(0,CellType.STRING).setCellValue(materialName)
            row.createCell(1,CellType.NUMERIC).setCellValue(quantity/1000)
            count++
        }
        headers.indices.forEach { separatedMaterialsSheet.autoSizeColumn(it) }
    }
}