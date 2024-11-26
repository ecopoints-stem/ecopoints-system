package br.edu.uea.ecopoints.repository.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.enums.material.MaterialType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialTypeRepository : JpaRepository<TypeOfMaterial, Long> {
    fun findByName(name: String) : TypeOfMaterial?
    fun findByNameStartingWithIgnoreCase(prefix: String): List<TypeOfMaterial>
    fun findByType(type: MaterialType): List<TypeOfMaterial>
}