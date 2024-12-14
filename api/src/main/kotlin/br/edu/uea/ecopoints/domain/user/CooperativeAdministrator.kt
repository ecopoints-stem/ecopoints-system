package br.edu.uea.ecopoints.domain.user

import br.edu.uea.ecopoints.domain.pickup.RecyclingPickupRequest
import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.enums.user.UserTypeRole.ROLE_ADMINISTRATOR
import br.edu.uea.ecopoints.view.user.CoopAdmView
import jakarta.persistence.*

@Entity
class CooperativeAdministrator (
    id: Long?=null,
    name: String,
    phone: String?,
    email: String,
    password: String,
    @Column(nullable = true)
    var securityQuestion: String? = null,
    @Column(nullable = true, length = 60)
    var securityResponse: String? = null,
    @OneToOne(mappedBy = "adm", fetch = FetchType.EAGER, optional = true, cascade =[CascadeType.PERSIST, CascadeType.MERGE]) // ERA LAZY, modifiquei
    var cooperative: Cooperative? = null,
    @OneToMany(
        fetch = FetchType.EAGER, // AQUI ERA LAZY, modifiquei
        mappedBy = "requester",
        cascade = [CascadeType.PERSIST,
            CascadeType.REMOVE]
    ) val pickupRequests: MutableSet<RecyclingPickupRequest> = mutableSetOf()
) : EcoUser(id = id, name, phone, email, password, role = ROLE_ADMINISTRATOR) {
    fun toAView() = CoopAdmView(
        id = this.id!!,
        name= this.name,
        phone = this.phone,
        email = this.email,
        securityQuestion = this.securityQuestion
    )
}