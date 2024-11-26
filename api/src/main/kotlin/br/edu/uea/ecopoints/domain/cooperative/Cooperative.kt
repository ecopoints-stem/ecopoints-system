package br.edu.uea.ecopoints.domain.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import jakarta.persistence.*

@Entity
class Cooperative (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 150)
    val name: String = "",
    @Column(nullable = false, length = 15, unique = true)
    val cnpj: String = "",
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    var adm: CooperativeAdministrator? = null,
    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "cooperative"
    ) val employees: MutableList<RecyclingSorter> = mutableListOf(),
    @ManyToMany(
        fetch = FetchType.EAGER,
        cascade = [CascadeType.PERSIST, CascadeType.MERGE]
    ) @JoinTable(
        name = "cooperative_material",
        joinColumns = [JoinColumn(name = "cooperative_id")],
        inverseJoinColumns = [JoinColumn(name = "material_id")]
    ) val materials: MutableList<TypeOfMaterial> = mutableListOf()
)