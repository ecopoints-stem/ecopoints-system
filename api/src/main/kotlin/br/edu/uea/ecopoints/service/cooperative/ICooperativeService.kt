package br.edu.uea.ecopoints.service.cooperative

import br.edu.uea.ecopoints.domain.cooperative.Cooperative

interface ICooperativeService {
    fun save(cooperative: Cooperative) : Cooperative
    fun findById(id: Long) : Cooperative
    fun findByCnpj(cnpj: String) : Cooperative
    fun existsById(id: Long) : Boolean
    fun existsByCpnj(cpnj: String) : Boolean
    fun deleteById(id: Long)
}