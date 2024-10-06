package br.edu.uea.ecopoints.domain.cooperative

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import jakarta.persistence.*

@Entity
class Cooperative (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 150)
    val name: String = "",
    @Column(nullable = false, length = 15)
    val cpnj: String = "",
    @OneToMany(fetch = FetchType.LAZY,
        mappedBy = "cooperative",
        cascade = [CascadeType.PERSIST, CascadeType.REMOVE]
    ) val employees: MutableList<RecyclingSorter> = mutableListOf()
)