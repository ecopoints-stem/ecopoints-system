package br.edu.uea.ecopoints.util

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.toMaterialType() : MaterialType = when(this){
    "PLÁSTICO" -> MaterialType.PLASTICS
    "PAPEL" -> MaterialType.PAPER
    "METAL" -> MaterialType.METALS
    "VIDRO" -> MaterialType.GLASS
    "ISOPOR" -> MaterialType.EXPANDED_POLYSTYRENE
    else -> MaterialType.WASTE
}

fun MaterialType.toMaterialString(): String = when (this) {
    MaterialType.PLASTICS -> "PLÁSTICO"
    MaterialType.PAPER -> "PAPEL"
    MaterialType.METALS -> "METAL"
    MaterialType.GLASS -> "VIDRO"
    MaterialType.EXPANDED_POLYSTYRENE -> "ISOPOR"
    else -> "LIXO"
}

fun String.toPickUpStatus() : PickupRequestStatus = when(this){
    "ACEITO" -> PickupRequestStatus.ACCEPTED
    "EM ANDAMENTO" -> PickupRequestStatus.IN_PROGRESS
    "COMPLETADO" -> PickupRequestStatus.COMPLETED
    "REJEITADO" -> PickupRequestStatus.REJECTED
    else -> PickupRequestStatus.IN_PROGRESS
}

fun PickupRequestStatus.toStatusString() : String = when(this){
    PickupRequestStatus.ACCEPTED -> "ACEITO"
    PickupRequestStatus.IN_PROGRESS -> "EM ANDAMENTO"
    PickupRequestStatus.COMPLETED -> "COMPLETADO"
    PickupRequestStatus.REJECTED -> "REJEITADO"
}

fun LocalDate.formatedPersonDate() : String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}

fun String?.toPersonDate(): LocalDate? {
    if (this == null) return null
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return try {
        LocalDate.parse(this, formatter)
    } catch (e: DateTimeParseException) {
        null
    }
}