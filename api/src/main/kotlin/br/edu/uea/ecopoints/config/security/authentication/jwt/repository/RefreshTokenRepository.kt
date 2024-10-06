package br.edu.uea.ecopoints.config.security.authentication.jwt.repository

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component

@Component
class RefreshTokenRepository {
    private val tokens = mutableMapOf<String,UserDetails>()

    fun findUserDetailsByToken(token: String) : UserDetails? = tokens[token]
    fun save(token: String, user: UserDetails) {
        tokens[token] = user

    }
}