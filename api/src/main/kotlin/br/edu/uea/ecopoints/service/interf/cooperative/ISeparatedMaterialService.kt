package br.edu.uea.ecopoints.service.interf.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial

interface ISeparatedMaterialService {
    fun save(separatedMaterial: SeparatedMaterial) : SeparatedMaterial
    fun findById(id: Long) : SeparatedMaterial
    fun deleteById(id: Long)
}