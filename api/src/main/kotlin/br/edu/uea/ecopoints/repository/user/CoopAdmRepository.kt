package br.edu.uea.ecopoints.repository.user

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CoopAdmRepository : JpaRepository<CooperativeAdministrator, Long> {
    @Query("""SELECT ca FROM CooperativeAdministrator ca 
        LEFT JOIN FETCH ca.cooperative 
        WHERE ca.id = :id
        """) fun findWithCooperative(@Param("id") id: Long): Optional<CooperativeAdministrator>
}