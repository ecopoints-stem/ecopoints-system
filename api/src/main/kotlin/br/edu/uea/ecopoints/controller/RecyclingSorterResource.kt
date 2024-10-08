package br.edu.uea.ecopoints.controller

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.dto.user.RecyclingSorterRegister
import br.edu.uea.ecopoints.service.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.user.IRecyclingSorterService
import br.edu.uea.ecopoints.view.user.RecyclingSorterView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/employee")
@Tag(name = "Recycling sorter resource")
class RecyclingSorterResource (
    private val recyclingSorterService: IRecyclingSorterService,
    private val cooperativeService: ICooperativeService,
    private val encoder: PasswordEncoder
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

        return ResponseEntity.status(HttpStatus.CREATED).body(recyclingSorterSaved?.toRView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<RecyclingSorterView>{
        val recyclingSorter = recyclingSorterService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(recyclingSorter.toRView())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        recyclingSorterService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação executada")
    }
}