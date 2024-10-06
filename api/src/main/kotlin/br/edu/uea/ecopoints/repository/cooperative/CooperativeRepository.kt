package br.edu.uea.ecopoints.repository.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CooperativeRepository  : JpaRepository<Cooperative, Long>{

}
