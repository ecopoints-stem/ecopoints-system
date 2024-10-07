package br.edu.uea.ecopoints.service.user

import br.edu.uea.ecopoints.domain.user.Driver

interface IDriverService {
    fun save(driver: Driver) : Driver
    fun findById(id: Long) : Driver
    fun existsById(id: Long) : Boolean
    fun deleteById(id: Long)
}