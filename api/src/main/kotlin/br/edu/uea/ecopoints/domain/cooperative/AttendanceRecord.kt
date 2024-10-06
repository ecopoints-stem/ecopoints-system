package br.edu.uea.ecopoints.domain.cooperative

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.enums.AttendanceRecordStatus
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalTime

@Entity
class AttendanceRecord (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val entryTime: LocalTime,
    @Column(nullable = true)
    var exitTime: LocalTime? = null,
    @Column(nullable = false) @Enumerated(EnumType.STRING)
    val status: AttendanceRecordStatus,
    @Column(nullable = false)
    val pDate: LocalDate,
    @JoinColumn(name = "cooperative_id", nullable = false)
    @ManyToOne(optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST]
    ) val cooperative: Cooperative,
    @JoinColumn(name = "recycling_sorter_id", nullable = false)
    @ManyToOne(optional = false,
        fetch = FetchType.LAZY,
        cascade = [CascadeType.PERSIST]
    ) val recyclingSorter: RecyclingSorter
)