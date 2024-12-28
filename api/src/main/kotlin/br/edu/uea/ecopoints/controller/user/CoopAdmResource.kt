package br.edu.uea.ecopoints.controller.user

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.dto.user.AdminUpdate
import br.edu.uea.ecopoints.dto.user.CoopAdmRegister
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.interf.cooperative.IMaterialService
import br.edu.uea.ecopoints.service.interf.cooperative.IReportService
import br.edu.uea.ecopoints.service.interf.user.ICoopAdmService
import br.edu.uea.ecopoints.utils.EmailTexts
import br.edu.uea.ecopoints.view.cooperative.BarData
import br.edu.uea.ecopoints.view.user.CoopAdmView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread


@RestController
@RequestMapping("/admin")
@Tag(name = "Cooperative Admin Resource")
class CoopAdmResource (
    private val coopAdmService: ICoopAdmService,
    private val cooperativeService: ICooperativeService,
    private val materialService: IMaterialService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService,
    private val reportService: IReportService,
){
    @PostMapping
    @Transactional
    fun save(@RequestBody @Valid coopAdmRegister: CoopAdmRegister) : ResponseEntity<CoopAdmView>{
        val coopAdm = coopAdmRegister.toEntity()
        coopAdm.password = encoder.encode(coopAdm.password)
        var coopAdmSaved : CooperativeAdministrator? = null
        if(coopAdmRegister.cooperativeCnpj!=null){
            if(cooperativeService.existsByCpnj(coopAdmRegister.cooperativeCnpj)){
                val cooperative = cooperativeService.findByCnpj(coopAdmRegister.cooperativeCnpj)
                coopAdm.cooperative = cooperative
                coopAdmSaved = coopAdmService.save(coopAdm)
                cooperative.adm = coopAdmSaved
                cooperativeService.save(cooperative)
            }
        } else {
            coopAdmSaved = coopAdmService.save(coopAdm)
        }
        coopAdmSaved?.let { adm ->
            thread(true){
                emailService.sendWelcomeMessage(
                    adm.email,
                    adm.name,
                    adm.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(coopAdmSaved?.toAView())
    }

    @GetMapping("/{id}/report")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun getReportExcel(@PathVariable id: Long, @RequestParam("endDate") endDate: LocalDateTime) {
        val adm = coopAdmService.findWithCooperative(id)
        adm.cooperative?.let {
            val cooperative = cooperativeService.findByIdWithAdminEmployeesAndMaterials(it.id!!)
            thread (start = true){
                val excel = reportService.generateAdminReport(id, endDate.minusMonths(1), endDate)
                emailService.sendExcelReport(adm.email,
                    EmailTexts.EXCEL_ADMIN_REPORT_SUBJECT,
                    EmailTexts.EXCEL_ADMIN_REPORT_BODY, excel,
                    "relatorio.xlsx"
                )
            }
        }
    }

    @GetMapping("/{id}/client/report")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun getClientReportExcel(
        @PathVariable id: Long,
        @RequestParam(required = true) clientCnpj: String,
        @RequestParam(required = true) startDate: String,
        @RequestParam(required = true) endDate: String) {

        val cooperative = cooperativeService.findByCnpjWithAdministrator(clientCnpj)
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val entityStartDate = LocalDate.parse(startDate,dateFormatter)
        val entityEndDate = LocalDate.parse(endDate, dateFormatter)
        val clientId = if(cooperative.adm!=null) cooperative.adm!!.id else null
        if(clientId!=null && id!=clientId){
            thread (start = true){
                val excel = reportService.generateClientReport(id, clientId, entityStartDate, entityEndDate)
                emailService.sendExcelReport(cooperative.adm!!.email,
                    EmailTexts.EXCEL_ADMIN_REPORT_SUBJECT,
                    EmailTexts.EXCEL_ADMIN_REPORT_BODY, excel,
                    "relatorio.xlsx"
                )
            }
        }
    }

    @GetMapping("/{id}/bar")
    fun getBarData(@PathVariable id: Long, @RequestParam("endDate") endDate: LocalDateTime) : ResponseEntity<BarData> {
        val adm = coopAdmService.findWithCooperative(id)
        if(adm.cooperative?.id==null){
            throw DomainException(message = "Você não possui cooperativa administrada para gerar relatório", type = ExceptionDetailsStatus.INVALID_INPUT)
        }
        val cooperative = cooperativeService.findById(adm.cooperative!!.id!!)
        val barData =  cooperativeService.findBarDataByCooperativeIdAndSeparatedDateBetween(cooperative.id!!, endDate.minusMonths(1), endDate)
        return ResponseEntity.status(HttpStatus.OK).body(barData)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<CoopAdmView>{
        val coopAdmSaved = coopAdmService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(coopAdmSaved.toAView())
    }
    @PostMapping("/{id}/material")
    @Transactional
    fun addNewMaterial(@PathVariable id: Long, @RequestBody material: TypeOfMaterial) : ResponseEntity<List<TypeOfMaterial>>{
        val coopAdm = coopAdmService.findById(id)

        if (material.id==null){
            var materialBD = materialService.findByName(material.name)
            if (materialBD==null){
                materialBD = materialService.save(material)
            }
            coopAdm.cooperative?.materials?.add(materialBD)
            coopAdmService.save(coopAdm)
        } else{
            val materialBD = materialService.findById(material.id)
            coopAdm.cooperative?.materials?.add(materialBD)
            coopAdmService.save(coopAdm)
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(coopAdm.cooperative?.materials?.toList())
    }

    @PatchMapping
    @Transactional
    fun updateAdmin(
        @RequestParam(value = "adminId") id: Long,
        @RequestBody @Valid dto: AdminUpdate
    ) : ResponseEntity<CoopAdmView> {
        val adm = coopAdmService.findById(id)
        val adminUp = dto.toEntity(adm)
        adminUp.password = encoder.encode(dto.password)
        adminUp.cooperative = adm.cooperative
        adminUp.pickupRequests.addAll(adm.pickupRequests)
        val adminUpdate = coopAdmService.save(adminUp)
        return ResponseEntity.status(HttpStatus.OK).body(adminUpdate.toAView())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        coopAdmService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação Executada")
    }
}