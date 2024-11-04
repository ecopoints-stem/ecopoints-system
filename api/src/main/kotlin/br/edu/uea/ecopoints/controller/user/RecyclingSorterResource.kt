package br.edu.uea.ecopoints.controller.user

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.dto.cooperative.AttendanceRegister
import br.edu.uea.ecopoints.dto.user.RecyclingSorterRegister
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus.INVALID_INPUT
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.service.cooperative.IAttendanceRecordService
import br.edu.uea.ecopoints.service.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.user.IRecyclingSorterService
import br.edu.uea.ecopoints.view.user.RecyclingSorterView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
    private val encoder: PasswordEncoder,
    private val emailService: EmailService
) {
    @PostMapping
    fun save(@RequestBody @Valid recyclingSorterRegister: RecyclingSorterRegister) : ResponseEntity<RecyclingSorterView>{
        val recyclingSorter = recyclingSorterRegister.toEntity()
        recyclingSorter.password = encoder.encode(recyclingSorter.password)
        var recyclingSorterSaved: RecyclingSorter? = null
        if(recyclingSorterRegister.cnpj!=null){
            if(cooperativeService.existsByCpnj(recyclingSorterRegister.cnpj)) {
                val cooperative = cooperativeService.findByCnpj(recyclingSorterRegister.cnpj)
                recyclingSorter.cooperative = cooperative
                recyclingSorterSaved = recyclingSorterService.save(recyclingSorter)
                cooperative.employees.add(recyclingSorterSaved)
                cooperativeService.save(cooperative)
            }
        } else {
            recyclingSorterSaved = recyclingSorterService.save(recyclingSorter)
        }
        recyclingSorterSaved?.let { employee ->
            thread(true){
                emailService.sendWelcomeMessage(
                    employee.email,
                    employee.name,
                    employee.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(recyclingSorterSaved?.toRView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<RecyclingSorterView>{
        val recyclingSorter = recyclingSorterService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(recyclingSorter.toRView())
    }

    @PostMapping("/{id}/attendance")
    fun saveAttendanceRecord(@PathVariable id: Long, @RequestBody info: AttendanceRegister) : ResponseEntity<AttendanceRecord?>{
        val employee = recyclingSorterService.findById(id)
        val lastAttendance = attendanceRecordService.findLastByEmployeeId(employee.id!!)
        var attendanceRecord: AttendanceRecord? = null
        val attendanceSp = attendanceRecordService.findByRecyclingSorterIdAndPDate(id,info.pDate)

        if(lastAttendance!=null){
            if(lastAttendance.exitTime==null && info.pDate.isAfter(lastAttendance.pDate)){
                throw DomainException("O ponto do dia ${lastAttendance.pDate} não foi fechado, favor corrigir com o adm",INVALID_INPUT)
            } else if(lastAttendance.exitTime==null && (info.pDate==lastAttendance.pDate && info.entryTime==lastAttendance.entryTime && info.exitTime!=null)){
                val cooperative = cooperativeService.findById(info.cooperativeId)
                attendanceSp?.let {
                    it.exitTime = info.exitTime
                    attendanceRecord = attendanceRecordService.save(it)
                }
            } else {
                val cooperative = cooperativeService.findById(info.cooperativeId)
                attendanceRecord = attendanceRecordService.save(
                    AttendanceRecord(
                        id = null,
                        entryTime = info.entryTime,
                        exitTime = info.exitTime,
                        status = info.status,
                        pDate = info.pDate,
                        cooperative = cooperative,
                        recyclingSorter = employee
                    )
                )
            }
        } else{
            //é o primeiro registro de ponto do funcionário na cooperativa
            val cooperative = cooperativeService.findById(info.cooperativeId)
            attendanceRecord = attendanceRecordService.save(
                AttendanceRecord(
                    id = null,
                    entryTime = info.entryTime,
                    exitTime = info.exitTime,
                    status = info.status,
                    pDate = info.pDate,
                    cooperative = cooperative,
                    recyclingSorter = employee
                )
            )
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceRecord)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        recyclingSorterService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação executada")
    }
}