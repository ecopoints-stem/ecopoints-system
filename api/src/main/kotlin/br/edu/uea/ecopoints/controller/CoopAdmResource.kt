package br.edu.uea.ecopoints.controller

import br.edu.uea.ecopoints.dto.user.CoopAdmRegister
import br.edu.uea.ecopoints.service.user.ICoopAdmService
import br.edu.uea.ecopoints.view.user.CoopAdmView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/admin")
@Tag(name = "Cooperative Admin Resource")
class CoopAdmResource (
    private val coopAdmService: ICoopAdmService,
    private val encoder: PasswordEncoder
){
    @PostMapping
    fun save(@RequestBody @Valid coopAdmRegister: CoopAdmRegister) : ResponseEntity<CoopAdmView>{
        val coopAdm = coopAdmRegister.toEntity()
        coopAdm.password = encoder.encode(coopAdm.password)
        val coopAdmSaved = coopAdmService.save(coopAdm)
        return ResponseEntity.status(HttpStatus.CREATED).body(coopAdmSaved.toView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<CoopAdmView>{
        val coopAdmSaved = coopAdmService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(coopAdmSaved.toView())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        coopAdmService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação Executada")
    }
}