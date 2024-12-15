package br.edu.uea.ecopoints.repository.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import org.springframework.data.jpa.repository.JpaRepository

interface SeparatedMaterialRepository : JpaRepository<SeparatedMaterial, Long> {

}