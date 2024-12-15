package br.edu.uea.ecopoints.domain.network.request

import br.edu.uea.ecopoints.domain.entity.enums.AttendanceRecordStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalTime

data class AttendanceRecordRegister(
    @JsonProperty("personDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") val personDate: LocalDate,
    @JsonProperty("entryTime") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") val entryTime: LocalTime,
    @JsonProperty("exitTime") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") val exitTime: LocalTime? = null,
    @JsonProperty("status") val status: AttendanceRecordStatus,
    @JsonProperty("cooperativeId") val cooperativeId: Long
)
