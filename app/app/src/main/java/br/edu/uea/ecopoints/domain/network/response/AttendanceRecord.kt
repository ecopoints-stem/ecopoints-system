package br.edu.uea.ecopoints.domain.network.response

import br.edu.uea.ecopoints.domain.entity.enums.AttendanceRecordStatus
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate
import java.time.LocalTime

data class AttendanceRecord(
    @JsonProperty("id") val id: Long,
    @JsonProperty("pDate") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") val pDate: LocalDate,
    @JsonProperty("entryTime") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") val entryTime: LocalTime,
    @JsonProperty("exitTime") @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss") val exitTime: LocalTime? = null,
    @JsonProperty("status") var status: AttendanceRecordStatus
)
