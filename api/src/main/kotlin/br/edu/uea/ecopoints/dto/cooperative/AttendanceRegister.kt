package br.edu.uea.ecopoints.dto.cooperative

import br.edu.uea.ecopoints.enums.AttendanceRecordStatus
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.annotation.Nullable
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalTime

data class AttendanceRegister(
    @field:NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") val personDate: LocalDate,
    @field:NotNull @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") val entryTime: LocalTime,
    @field:Nullable @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") val exitTime: LocalTime? = null,
    @field:NotNull val status: AttendanceRecordStatus,
    @field:NotNull val cooperativeId: Long
)
