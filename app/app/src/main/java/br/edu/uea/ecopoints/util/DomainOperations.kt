package br.edu.uea.ecopoints.util

import br.edu.uea.ecopoints.domain.entity.enums.MaterialType

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
