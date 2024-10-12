package br.edu.uea.ecopoints.domain.user

import br.edu.uea.ecopoints.domain.RecyclingPickupRequest
import br.edu.uea.ecopoints.domain.cooperative.Cooperative
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
    @Column(nullable = true)
    var securityQuestion: String? = null,
    @Column(nullable = true, length = 60)
    var securityResponse: String? = null,
    @OneToOne(mappedBy = "adm", fetch = FetchType.LAZY, optional = true, cascade =[CascadeType.PERSIST])
    var cooperative: Cooperative? = null,
    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "requester",
        cascade = [CascadeType.PERSIST,
            CascadeType.REMOVE]
    ) val pickupRequests: MutableList<RecyclingPickupRequest> = mutableListOf()
) : EcoUser(id = null, name, phone, email, password, role = ROLE_ADMINISTRATOR) {
    fun toAView() = CoopAdmView(
        id = this.id!!,
        name= this.name,
        phone = this.phone,
        email = this.email,
        securityQuestion = this.securityQuestion
    )
}