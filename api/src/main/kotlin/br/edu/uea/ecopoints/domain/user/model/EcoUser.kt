package br.edu.uea.ecopoints.domain.user.model

import br.edu.uea.ecopoints.enums.user.UserTypeRole.ROLE_UNDEFINED
import br.edu.uea.ecopoints.enums.user.UserTypeRole
import br.edu.uea.ecopoints.view.user.UserView
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
open class EcoUser (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long? = null,
    @Column(length = 210, nullable = false)
    open val name: String,
    @Column(length = 13, nullable = true)
    open val phone: String?,
    @Column(length = 100,unique = true, nullable = false)
    open val email: String,
    @Column(nullable = false, length = 80)
    open var password: String,
    @Column(nullable = false)
    open var isPasswordRecovery: Boolean = false,
    @Enumerated(EnumType.STRING)
    open var role: UserTypeRole
) {
    fun toView() = UserView(
        id = this.id!!,
        name = this.name,
        phone = this.phone,
        email = this.email,
        isPasswordRecovery = this.isPasswordRecovery,
        role = this.role.toString()
    )
}