package br.edu.uea.ecopoints.repository.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CooperativeRepository  : JpaRepository<Cooperative, Long>{
    fun findByCnpj(cnpj: String) : Cooperative?
    @Query("SELECT c FROM Cooperative c LEFT JOIN FETCH c.employees WHERE c.cnpj = :cnpj")
    fun findByCnpjWithEmployees(@Param("cnpj") cnpj: String) : Cooperative?
    @Query("""
        SELECT c 
        FROM Cooperative c
        LEFT JOIN FETCH c.adm a
        LEFT JOIN FETCH c.employees e
        LEFT JOIN FETCH c.materials m
        WHERE c.id = :id
    """)
    fun findByIdWithAdminEmployeesAndMaterials(@Param("id") id: Long): Optional<Cooperative>
    @Query("""
        SELECT c 
        FROM Cooperative c 
        LEFT JOIN FETCH c.adm 
        WHERE c.cnpj = :cnpj
    """)
    fun findByCnpjWithAdministrator(@Param("cnpj") cnpj: String): Optional<Cooperative>
    fun existsByCnpj(cnpj: String) : Boolean
}
