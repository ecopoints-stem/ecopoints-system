package br.edu.uea.ecopoints.repository.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MaterialTypeRepository : JpaRepository<TypeOfMaterial, Long> {

}