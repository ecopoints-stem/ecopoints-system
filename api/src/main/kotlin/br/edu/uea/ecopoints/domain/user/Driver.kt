package br.edu.uea.ecopoints.domain.user

import br.edu.uea.ecopoints.domain.RecyclingPickupRequest
import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.enums.user.UserTypeRole.ROLE_DRIVER
import jakarta.persistence.*

@Entity
class Driver (
    name: String,
    phone: String?,
    email: String,
    password: String,
    @Column(nullable = false, length = 10, unique = true)
    val cnh: String,
    @Column(nullable = false, length = 11, unique = true)
    val cpf: String,
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "driver",
        cascade = [CascadeType.PERSIST,
            CascadeType.REMOVE])
    val pickupRequests: MutableList<RecyclingPickupRequest> = mutableListOf()
) : EcoUser(id = null, name, phone, email, password, role = ROLE_DRIVER) {
    constructor() : this("","","","","","", mutableListOf())
}