package br.edu.uea.ecopoints.repository.pickup

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RecPickupRequestRepository : JpaRepository<RecyclingPickupRequest, Long> {
    @Query("""
        SELECT r 
        FROM RecyclingPickupRequest r 
        LEFT JOIN FETCH r.driver d 
        JOIN FETCH r.requester a 
        WHERE r.id = :id
    """)
    fun findByIdWithDriverAndRequester(@Param("id") id: Long): Optional<RecyclingPickupRequest>
    @Query("""
        SELECT r 
        FROM RecyclingPickupRequest r 
        LEFT JOIN FETCH r.driver d 
        JOIN FETCH r.requester a 
        WHERE d.id = :driverId
        ORDER BY r.pDate DESC
    """)
    fun findAllByDriverIdWithDriverAndRequester(
        @Param("driverId") driverId: Long
    ): List<RecyclingPickupRequest>

    @Query("""
        SELECT COUNT(r)
        FROM RecyclingPickupRequest r
        WHERE r.driver.id = :driverId
    """)
    fun countByDriverId(@Param("driverId") driverId: Long): Long

    @Query("""
        SELECT r
        FROM RecyclingPickupRequest r
        LEFT JOIN FETCH r.driver d
        JOIN FETCH r.requester a
        WHERE a.id = :requesterId
        ORDER BY r.pDate DESC
    """)
    fun findAllByRequesterIdWithDriverAndRequester(
        @Param("requesterId") requesterId: Long
    ): List<RecyclingPickupRequest>

    @Query("""
        SELECT COUNT(r)
        FROM RecyclingPickupRequest r
        WHERE r.requester.id = :requesterId
    """)
    fun countByRequesterId(@Param("requesterId") requesterId: Long): Long
}