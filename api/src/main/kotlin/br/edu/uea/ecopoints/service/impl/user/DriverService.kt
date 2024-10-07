package br.edu.uea.ecopoints.service.impl.user

import br.edu.uea.ecopoints.domain.user.Driver
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.user.DriverRepository
import br.edu.uea.ecopoints.service.user.IDriverService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class DriverService (
    private val driverRepository: DriverRepository
) : IDriverService {
    @Transactional
    override fun save(driver: Driver) : Driver = driverRepository.save(driver)
    override fun findById(id: Long): Driver = driverRepository.findById(id).orElseThrow {
        throw DomainException("Usuário do tipo Driver com id $id não encontrado", ExceptionDetailsStatus.USER_NOT_FOUND)
    }
    override fun existsById(id: Long): Boolean = driverRepository.existsById(id)
    override fun deleteById(id: Long) {
        driverRepository.deleteById(id)
    }
}