package br.edu.uea.ecopoints.service.impl.cooperative

import br.edu.uea.ecopoints.domain.cooperative.material.TypeOfMaterial
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.enums.material.MaterialType
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.cooperative.material.MaterialTypeRepository
import br.edu.uea.ecopoints.service.cooperative.IMaterialService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class MaterialService (
    private val materialRepository: MaterialTypeRepository
) : IMaterialService {
    @Transactional
    override fun save(material: TypeOfMaterial): TypeOfMaterial = this.materialRepository.save(material)

    override fun findById(id: Long): TypeOfMaterial = this.materialRepository.findById(id).orElseThrow{
        throw DomainException("Material com id $id não encontrado", ExceptionDetailsStatus.INVALID_INPUT)
    }

    override fun findByName(name: String): TypeOfMaterial? = materialRepository.findByName(name)

    override fun findByNameStartingWithIgnoreCase(prefix: String): List<TypeOfMaterial> = this.materialRepository.findByNameStartingWithIgnoreCase(prefix)
    override fun findByType(type: MaterialType): List<TypeOfMaterial> = this.materialRepository.findByType(type)

}