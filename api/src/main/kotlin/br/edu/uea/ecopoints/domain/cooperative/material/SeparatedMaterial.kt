package br.edu.uea.ecopoints.domain.cooperative.material

import br.edu.uea.ecopoints.domain.cooperative.Cooperative
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

@Entity
class SeparatedMaterial (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @NotNull
    val separatedDate: LocalDateTime,
    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.MERGE]
    ) @JoinColumn(
        name = "employee_id",
        nullable = true
    ) var employee: RecyclingSorter? = null,
    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER,
        cascade = [CascadeType.MERGE]
    ) @JoinColumn(
        name = "material_id",
        nullable = true
    ) var typeOfMaterial: TypeOfMaterial? = null,
    @ManyToOne(
        optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.MERGE]
    ) @JoinColumn(
        name = "cooperative_id",
        nullable = true
    ) var cooperative: Cooperative? = null,
    @NotNull
    val quantity: Double
)