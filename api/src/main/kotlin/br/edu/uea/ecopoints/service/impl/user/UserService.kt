package br.edu.uea.ecopoints.service.impl.user

import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.repository.user.model.EcoUserRepository
import br.edu.uea.ecopoints.service.user.IUserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: EcoUserRepository
) : IUserService {
    @Transactional
    override fun save(user: EcoUser): EcoUser = userRepository.save(user)

    override fun findById(id: Long): EcoUser = userRepository.findById(id).orElseThrow{
        throw DomainException("Usuário com id $id não encontrado",ExceptionDetailsStatus.USER_NOT_FOUND)
    }

    override fun existsById(id: Long): Boolean = userRepository.existsById(id)

    override fun findByEmail(email: String): EcoUser = userRepository.findByEmail(email) ?: throw RuntimeException("Erro, não encontrado")

    @Transactional
    override fun resetPassword(user: EcoUser, newPassword: String): EcoUser {
        user.password = newPassword
        user.isPasswordRecovery = true
        val userUpdated = this.userRepository.save(user)
        return userUpdated
    }

}