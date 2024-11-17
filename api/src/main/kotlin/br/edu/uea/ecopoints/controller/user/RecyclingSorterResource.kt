package br.edu.uea.ecopoints.controller.user

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.dto.cooperative.AttendanceRegister
import br.edu.uea.ecopoints.dto.user.RecyclingSorterRegister
import br.edu.uea.ecopoints.enums.AttendanceRecordStatus
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
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
        //TODO: Criar coisas aqui
        if (employee.cooperative==null)
            throw DomainException("Você não está associado a uma cooperativa para conseguir bater ponto, verifique com Adm",ExceptionDetailsStatus.INVALID_INPUT)
        if(lastAttendance==null){
            // Primeiro registro de trabalho do employee nessa cooperativa
            attendanceRecord = attendanceRecordService.save(
                AttendanceRecord(id=null, pDate = info.personDate ,entryTime = info.entryTime, exitTime = info.exitTime, status = info.status, cooperative = employee.cooperative!!, recyclingSorter = employee)
            )
        } else{
            if(info.personDate.isBefore(lastAttendance.pDate) && lastAttendance.exitTime==null){
                lastAttendance.status = AttendanceRecordStatus.ABSENT
                attendanceRecordService.save(lastAttendance)
                throw DomainException(message = "Você não bateu o horário de saída em ${lastAttendance.pDate}",ExceptionDetailsStatus.INVALID_INPUT)
            } else if(info.personDate==lastAttendance.pDate){
                //As datas coincidem, como já tinha um desse com a mesma data só pode ser o registro de saída
                if(lastAttendance.exitTime==null && info.entryTime==lastAttendance.entryTime && info.exitTime!=null){
                    lastAttendance.exitTime = info.exitTime
                    attendanceRecordService.save(lastAttendance)
                } else {
                    throw DomainException("Apenas um registro de trabalho por dia, tente novament amanhã", ExceptionDetailsStatus.INVALID_INPUT)
                }
            } else{
                attendanceRecord = attendanceRecordService.save(
                    AttendanceRecord(id=null, pDate = info.personDate ,entryTime = info.entryTime, exitTime = info.exitTime, status = info.status, cooperative = employee.cooperative!!, recyclingSorter = employee)
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceRecord)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        recyclingSorterService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação executada")
    }
}