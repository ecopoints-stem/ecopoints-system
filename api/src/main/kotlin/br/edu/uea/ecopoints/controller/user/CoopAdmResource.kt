package br.edu.uea.ecopoints.controller.user

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.dto.user.CoopAdmRegister
import br.edu.uea.ecopoints.service.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.user.ICoopAdmService
import br.edu.uea.ecopoints.view.user.CoopAdmView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import kotlin.concurrent.thread


@RestController
@RequestMapping("/admin")
@Tag(name = "Cooperative Admin Resource")
class CoopAdmResource (
    private val coopAdmService: ICoopAdmService,
    private val cooperativeService: ICooperativeService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService
){
    @PostMapping
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

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<CoopAdmView>{
        val coopAdmSaved = coopAdmService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(coopAdmSaved.toAView())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        coopAdmService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação Executada")
    }
}