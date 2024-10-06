package br.edu.uea.ecopoints.domain.user

import br.edu.uea.ecopoints.domain.cooperative.AttendanceRecord
import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.enums.user.UserTypeRole.ROLE_EMPLOYEE
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class RecyclingSorter (
    name: String,
    phone: String?,
    email: String,
    password: String,
    @Column(nullable = false, length = 11, unique = true)
    val cpf: String,
    @JoinColumn(name = "cooperative_id", nullable = true)
    @ManyToOne(optional = true,
        cascade = [CascadeType.PERSIST,CascadeType.REFRESH]
    ) var cooperative: Cooperative? = null,
    @OneToMany(
        mappedBy = "recyclingSorter",
        cascade = [CascadeType.PERSIST,CascadeType.REFRESH],
        fetch = FetchType.LAZY
    ) var records: MutableList<AttendanceRecord> = mutableListOf()
) : EcoUser(id = null, name, phone, email, password, role = ROLE_EMPLOYEE)