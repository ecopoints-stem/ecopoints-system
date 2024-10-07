package br.edu.uea.ecopoints.config.security.userdetails

import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.repository.user.model.EcoUserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService (
    private val userRepository: EcoUserRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user : EcoUser = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Usuário com email $username não encontrado")
        return User(user.email,user.password,
            true,true,
            !user.isPasswordRecovery, true,
            mutableListOf(SimpleGrantedAuthority(user.role.toString())))
    }
}