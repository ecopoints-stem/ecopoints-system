package br.edu.uea.ecopoints.repository.user

import br.edu.uea.ecopoints.domain.user.Driver
import jakarta.validation.constraints.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DriverRepository : JpaRepository<Driver, Long> {
    fun findByEmail(email: String) : Driver?
}