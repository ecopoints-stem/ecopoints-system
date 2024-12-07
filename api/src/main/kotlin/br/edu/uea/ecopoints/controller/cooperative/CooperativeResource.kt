package br.edu.uea.ecopoints.controller.cooperative

import br.edu.uea.ecopoints.dto.cooperative.CooperativeRegister
import br.edu.uea.ecopoints.service.interf.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.interf.user.ICoopAdmService
import br.edu.uea.ecopoints.service.interf.user.IRecyclingSorterService
import br.edu.uea.ecopoints.view.cooperative.CooperativeView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cooperative")
@Tag(name = "Cooperative Resource")
class CooperativeResource (
    private val cooperativeService: ICooperativeService,
    private val coopAdminService: ICoopAdmService,
    private val employeeService: IRecyclingSorterService
) {
    @PostMapping
    fun create(@RequestBody @Valid dto: CooperativeRegister) : ResponseEntity<CooperativeView>{
        val cooperative = dto.toEntity()
        if(dto.adminId!=null && coopAdminService.existsById(dto.adminId!!)){
            cooperative.adm = coopAdminService.findById(dto.adminId!!)
            cooperative.employees.addAll(dto.employeesId.map { id -> employeeService.findById(id) })
        }
        cooperativeService.save(cooperative)
        val cooperativeSaved = cooperativeService.findByIdWithEmployeesAndMaterials(cooperative.id!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(cooperativeSaved.toView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<CooperativeView>{
        val cooperative = cooperativeService.findByIdWithEmployeesAndMaterials(id)
        return ResponseEntity.status(HttpStatus.OK).body(cooperative.toView())
    }

    /*@PostMapping("/{id}/newOwner")
    fun addNewOwner(@PathVariable id: Long, @RequestParam("ownerId") ownerId: Long) : ResponseEntity<CooperativeView>{

    }*/
}