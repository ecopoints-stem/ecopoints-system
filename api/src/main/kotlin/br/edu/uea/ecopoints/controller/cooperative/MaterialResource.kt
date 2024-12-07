package br.edu.uea.ecopoints.controller.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.dto.cooperative.Material
import br.edu.uea.ecopoints.enums.material.MaterialType
import br.edu.uea.ecopoints.service.interf.cooperative.IMaterialService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/material")
@Tag(name = "Material Resource")
class MaterialResource (
    private val materialService: IMaterialService,
) {

    @PostMapping
    fun createNewMaterial(@RequestBody @Valid dto: Material) : ResponseEntity<TypeOfMaterial> {
        val material = materialService.save(dto.toEntity())
        return ResponseEntity.status(HttpStatus.CREATED).body(material)
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<TypeOfMaterial>{
        val material = materialService.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(material)
    }

    @GetMapping
    fun findByNameStartingWith(@RequestParam("name") name: String) : ResponseEntity<List<TypeOfMaterial>> {
        val list = materialService.findByNameStartingWithIgnoreCase(name)
        return ResponseEntity.status(HttpStatus.OK).body(list)
    }

    @GetMapping("/{type}")
    fun findByType(@PathVariable type: MaterialType) : ResponseEntity<List<TypeOfMaterial>> {
        val list = materialService.findByType(type)
        return ResponseEntity.status(HttpStatus.OK).body(list)
    }

}