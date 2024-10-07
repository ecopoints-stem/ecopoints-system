package br.edu.uea.ecopoints.service

import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.domain.user.Driver

interface ICoopAdmService {
    fun save(driver: CooperativeAdministrator) : CooperativeAdministrator
    fun findById(id: Long) : CooperativeAdministrator
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}