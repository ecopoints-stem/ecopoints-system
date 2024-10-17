package br.edu.uea.ecopoints.service.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.enums.material.MaterialType

interface IMaterialService {
    fun save(material: TypeOfMaterial) : TypeOfMaterial
    fun findById(id: Long) : TypeOfMaterial
    fun findByNameStartingWithIgnoreCase(prefix: String): List<TypeOfMaterial>
    fun findByType(type: MaterialType): List<TypeOfMaterial>
}