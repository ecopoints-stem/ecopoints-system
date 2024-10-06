package br.edu.uea.ecopoints.service.impl

import br.edu.uea.ecopoints.domain.user.Driver
import br.edu.uea.ecopoints.repository.user.DriverRepository
import br.edu.uea.ecopoints.service.IDriverService
import org.springframework.stereotype.Service

@Service
class DriverService (
    private val driverRepository: DriverRepository
) : IDriverService {
    override fun save(driver: Driver) : Driver = driverRepository.save(driver)
    override fun findById(id: Long): Driver = driverRepository.findById(id).orElseThrow {
        throw RuntimeException("Não encontrado")
    }
    override fun existsById(id: Long): Boolean = driverRepository.existsById(id)
    override fun deleteById(id: Long) {
        driverRepository.deleteById(id)
    }
}