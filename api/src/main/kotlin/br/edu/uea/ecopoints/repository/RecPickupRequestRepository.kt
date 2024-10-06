package br.edu.uea.ecopoints.repository

import br.edu.uea.ecopoints.domain.RecyclingPickupRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecPickupRequestRepository : JpaRepository<RecyclingPickupRequest, Long> {
}