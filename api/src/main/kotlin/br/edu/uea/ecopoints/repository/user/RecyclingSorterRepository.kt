package br.edu.uea.ecopoints.repository.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecyclingSorterRepository : JpaRepository<RecyclingSorter, Long>{

}