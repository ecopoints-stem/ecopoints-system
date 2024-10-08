package br.edu.uea.ecopoints.controller.user

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.dto.user.DriverRegister
import br.edu.uea.ecopoints.service.user.IDriverService
import br.edu.uea.ecopoints.view.user.DriverView
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
import kotlin.concurrent.thread

@RestController
@RequestMapping("/driver")
@Tag(name = "Driver Resource")
class DriverResource (
    private val driverService: IDriverService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService
){
    @PostMapping
    fun save(@RequestBody @Valid driverDTO: DriverRegister) : ResponseEntity<DriverView>{
        val driver = driverDTO.toEntity()
        driver.password = encoder.encode(driver.password)
        val driverSaved = driverService.save(driver)
        driverSaved.let {
            thread(true){
                emailService.sendWelcomeMessage(
                    it.email,
                    it.name,
                    it.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(driverSaved.toDView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<DriverView>{
        val driver = driverService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(driver.toDView())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        driverService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação Executada")
    }
}