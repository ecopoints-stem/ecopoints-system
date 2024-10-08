package br.edu.uea.ecopoints.service.impl.user

import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.user.RecyclingSorterRepository
import br.edu.uea.ecopoints.service.user.IRecyclingSorterService
import org.springframework.stereotype.Service

@Service
class RecyclingSorterService(
    private val recyclingSorterRepository: RecyclingSorterRepository
) : IRecyclingSorterService {
    override fun save(recyclingSorter: RecyclingSorter): RecyclingSorter = this.recyclingSorterRepository.save(recyclingSorter)

    override fun findById(id: Long): RecyclingSorter = this.recyclingSorterRepository.findById(id).orElseThrow{
        throw DomainException("Usuário do tipo RecyclingSorter com id $id não encontrado", ExceptionDetailsStatus.USER_NOT_FOUND)
    }

    override fun existsById(id: Long): Boolean = this.recyclingSorterRepository.existsById(id)

    override fun deleteById(id: Long) {
        this.recyclingSorterRepository.deleteById(id)
    }
}