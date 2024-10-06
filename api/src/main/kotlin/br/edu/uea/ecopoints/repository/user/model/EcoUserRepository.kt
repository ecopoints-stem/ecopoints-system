package br.edu.uea.ecopoints.repository.user.model

import br.edu.uea.ecopoints.domain.user.model.EcoUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EcoUserRepository : JpaRepository<EcoUser,Long> {
    fun findByEmail(email: String) : EcoUser?
}