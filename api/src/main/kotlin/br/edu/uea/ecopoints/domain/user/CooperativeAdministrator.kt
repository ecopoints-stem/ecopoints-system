package br.edu.uea.ecopoints.domain.user

import br.edu.uea.ecopoints.domain.RecyclingPickupRequest
import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.enums.user.UserTypeRole.ROLE_ADMINISTRATOR
import br.edu.uea.ecopoints.view.user.CoopAdmView
import jakarta.persistence.*

@Entity
class CooperativeAdministrator (
    name: String,
    phone: String?,
    email: String,
    password: String,
    @Column(nullable = false)
    var securityQuestion: String? = null,
    @Column(nullable = false, length = 60)
    var securityResponse: String? = null,
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "requester",
        cascade = [CascadeType.PERSIST,
            CascadeType.REMOVE]
    ) val pickupRequests: MutableList<RecyclingPickupRequest> = mutableListOf()
) : EcoUser(id = null, name, phone, email, password, role = ROLE_ADMINISTRATOR) {
    fun toView() = CoopAdmView(
        id = this.id!!,
        name= this.name,
        phone = this.phone,
        email = this.email,
        securityQuestion = this.securityQuestion
    )
}