package br.edu.uea.ecopoints.domain.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.enums.material.MaterialType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
class TypeOfMaterial(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false, length = 160)
    val name: String,
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    val type: MaterialType,
    @JsonIgnore
    @ManyToMany(mappedBy = "material", fetch = FetchType.LAZY)
    val cooperatives : List<Cooperative> = mutableListOf()
)
