package br.edu.uea.ecopoints.repository.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RecyclingSorterRepository : JpaRepository<RecyclingSorter, Long>{
    @Query("""
        SELECT rs FROM RecyclingSorter rs 
        LEFT JOIN FETCH rs.cooperative 
        WHERE rs.id = :id """)
    fun findByIdWithCooperative(@Param("id") id: Long): Optional<RecyclingSorter>
}