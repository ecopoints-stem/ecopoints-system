package br.edu.uea.ecopoints.domain.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.enums.material.MaterialType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.annotation.Nullable
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@Entity
class TypeOfMaterial(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Nullable
    val id: Long? = null,
    @Column(nullable = false, length = 160, unique = true) @NotNull
    val name: String,
    @Column(nullable = false) @Enumerated(EnumType.STRING) @NotNull
    val type: MaterialType,
    @JsonIgnore
    @ManyToMany(mappedBy = "materials", fetch = FetchType.LAZY)
    val cooperatives : Set<Cooperative> = mutableSetOf()
)
