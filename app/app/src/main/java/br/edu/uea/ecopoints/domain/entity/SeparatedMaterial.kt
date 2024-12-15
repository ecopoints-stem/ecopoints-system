package br.edu.uea.ecopoints.domain.entity

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class SeparatedMaterial(
    @JsonProperty("id") val id: Long,
    @JsonProperty("separatedDate")
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd/MM/yyyy HH:mm:ss"
    ) val separatedDate: LocalDateTime,
    @JsonProperty("employeeId") val employeeId: Long,
    @JsonProperty("materialId") val materialId: Long,
    @JsonProperty("materialType") val materialType: MaterialType,
    @JsonProperty("materialName") val materialName: String,
    @JsonProperty("quantity") val quantity: Double
)
