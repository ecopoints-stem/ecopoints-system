package br.edu.uea.ecopoints.service.impl.user

import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.repository.user.model.EcoUserRepository
import br.edu.uea.ecopoints.service.user.IUserService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: EcoUserRepository
) : IUserService {
    override fun findById(id: Long): EcoUser = userRepository.findById(id).orElseThrow{
        throw RuntimeException("Não encontrado")
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