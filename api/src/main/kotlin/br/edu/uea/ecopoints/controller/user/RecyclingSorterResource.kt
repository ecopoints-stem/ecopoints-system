package br.edu.uea.ecopoints.controller.user

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.dto.cooperative.AttendanceRegister
import br.edu.uea.ecopoints.dto.cooperative.SeparatedMaterialRegister
import br.edu.uea.ecopoints.dto.user.RecyclingSorterRegister
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.service.interf.cooperative.IAttendanceRecordService
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.interf.cooperative.IMaterialService
import br.edu.uea.ecopoints.service.interf.cooperative.ISeparatedMaterialService
import br.edu.uea.ecopoints.service.interf.user.IRecyclingSorterService
import br.edu.uea.ecopoints.view.cooperative.SeparatedMaterialView
import br.edu.uea.ecopoints.view.user.RecyclingSorterView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import kotlin.concurrent.thread

@RestController
@RequestMapping("/employee")
@Tag(name = "Recycling sorter resource")
class RecyclingSorterResource (
    private val recyclingSorterService: IRecyclingSorterService,
    private val cooperativeService: ICooperativeService,
    private val attendanceRecordService: IAttendanceRecordService,
    private val materialService: IMaterialService,
    private val materialSeparatedService: ISeparatedMaterialService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService
) {
    @PostMapping
    fun save(@RequestBody @Valid recyclingSorterRegister: RecyclingSorterRegister) : ResponseEntity<RecyclingSorterView>{
        val recyclingSorter = recyclingSorterRegister.toEntity()
        recyclingSorter.password = encoder.encode(recyclingSorter.password)
        val recyclingSorterSaved: RecyclingSorter?
        if(recyclingSorterRegister.cnpj!=null){
            if(cooperativeService.existsByCpnj(recyclingSorterRegister.cnpj)) {
                recyclingSorterSaved = recyclingSorterService.save(recyclingSorter)
                val cooperative = cooperativeService.findByCnpjWithEmployees(recyclingSorterRegister.cnpj)
                recyclingSorterSaved.cooperative = cooperative
                cooperative.employees.add(recyclingSorterSaved)
                cooperativeService.save(cooperative)
                recyclingSorterService.save(recyclingSorterSaved)
            } else {
                throw DomainException(message = "CPNJ ${recyclingSorterRegister.cnpj} não cadastrado",ExceptionDetailsStatus.INVALID_INPUT)
            }
        } else {
            recyclingSorterSaved = recyclingSorterService.save(recyclingSorter)
        }
        recyclingSorterSaved.let { employee ->
            thread(true){
                emailService.sendWelcomeMessage(
                    employee.email,
                    employee.name,
                    employee.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(recyclingSorterSaved.toRView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<RecyclingSorterView>{
        val recyclingSorter = recyclingSorterService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(recyclingSorter.toRView())
    }

    @PostMapping("/{id}/attendance")
    fun saveAttendanceRecord(@PathVariable id: Long, @RequestBody info: AttendanceRegister) : ResponseEntity<AttendanceRecord?>{
        return ResponseEntity.status(HttpStatus.CREATED).body(null)
    }

    @PostMapping("/{id}/separated")
    fun saveSeparatedMaterial(@PathVariable id: Long, @RequestBody register: SeparatedMaterialRegister) : ResponseEntity<SeparatedMaterialView>{
        val separatedMaterial = register.toEntity()

        val typeOfMaterial = materialService.findById(register.materialId)
        val employee = recyclingSorterService.findById(id)

        separatedMaterial.employee = employee
        separatedMaterial.typeOfMaterial = typeOfMaterial
        val db = materialSeparatedService.save(separatedMaterial)
        val view = SeparatedMaterialView(
            id = db.id!!,
            separatedDate = db.separatedDate,
            employeeId = id,
            materialId = typeOfMaterial.id!!,
            materialType = typeOfMaterial.type,
            materialName = typeOfMaterial.name,
            quantity = db.quantity
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(view)
    }
    @GetMapping("/materials")
    fun getSeparatedMaterials(
        @RequestParam("employeeId") employeeId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "5") size: Int
    ) : Page<SeparatedMaterialView> {
        val pageable = PageRequest.of(page, size)
        val pgResult = materialSeparatedService.findAllByEmployeeId(employeeId,pageable)
        val pgResultView = pgResult.map { spMaterial ->
            SeparatedMaterialView(
                id = spMaterial.id!!,
                separatedDate = spMaterial.separatedDate,
                employeeId = employeeId,
                materialId = spMaterial.typeOfMaterial!!.id!!,
                materialType = spMaterial.typeOfMaterial!!.type,
                materialName = spMaterial.typeOfMaterial!!.name,
                quantity = spMaterial.quantity
            )
        }
        return pgResultView
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        recyclingSorterService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação executada")
    }
}