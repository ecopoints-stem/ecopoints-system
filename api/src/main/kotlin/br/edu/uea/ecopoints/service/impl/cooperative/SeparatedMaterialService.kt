package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.SeparatedMaterial
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.cooperative.material.SeparatedMaterialRepository
import br.edu.uea.ecopoints.service.interf.cooperative.ISeparatedMaterialService
import org.springframework.stereotype.Service

@Service
class SeparatedMaterialService (
    private val repo: SeparatedMaterialRepository
): ISeparatedMaterialService {
    override fun save(separatedMaterial: SeparatedMaterial): SeparatedMaterial = repo.save(separatedMaterial)

    override fun findById(id: Long): SeparatedMaterial = repo.findById(id).orElseThrow {
        throw DomainException("Material Separado com id $id não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun deleteById(id: Long) {
        repo.deleteById(id)
    }

}