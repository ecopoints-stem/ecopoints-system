package br.edu.uea.ecopoints.controller

import br.edu.uea.ecopoints.dto.user.DriverRegister
import br.edu.uea.ecopoints.service.user.IDriverService
import br.edu.uea.ecopoints.view.user.DriverView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/driver")
@Tag(name = "Driver Resource")
class DriverResource (
    private val driverService: IDriverService
){
    @PostMapping
    fun save(@RequestBody @Valid driverDTO: DriverRegister) : ResponseEntity<DriverView>{
        val driver = driverDTO.toEntity()
        val driverSaved = driverService.save(driver)
        return ResponseEntity.status(HttpStatus.CREATED).body(driverSaved.toView())
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<DriverView>{
        val driver = driverService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(driver.toView())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) : ResponseEntity<String>{
        driverService.deleteById(id)
        return ResponseEntity.status(HttpStatus.OK).body("Operação Executada")
    }
}