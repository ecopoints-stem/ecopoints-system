package br.edu.uea.ecopoints.config.security.authentication.service

import br.edu.uea.ecopoints.config.security.authentication.jwt.JwtProperties
import br.edu.uea.ecopoints.config.security.authentication.jwt.repository.RefreshTokenRepository
import br.edu.uea.ecopoints.config.security.authentication.request.LoginRequest
import br.edu.uea.ecopoints.config.security.authentication.response.LoginResponse
import br.edu.uea.ecopoints.repository.user.model.EcoUserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val authManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val userRepository: EcoUserRepository,
    private val tokenService: TokenService,
    private val tokenRefreshRepository: RefreshTokenRepository,
    private val jwtProperties: JwtProperties
) {
    fun authentication(authRequest: LoginRequest) : LoginResponse {
        authManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authRequest.email,
                authRequest.password
            )
        )
        val userFound = userDetailsService.loadUserByUsername(authRequest.email)
        val userEntity = userRepository.findByEmail(authRequest.email)

        val accessToken = createAccessToken(userFound)
        val refreshToken = createRefreshToken(userFound)
        tokenRefreshRepository.save(refreshToken,userFound)
        return LoginResponse(
            id = userEntity?.id,
            accessToken = accessToken,
            role = userEntity?.role.toString(),
            refreshToken = refreshToken,
            isPasswordRecovery = userEntity?.isPasswordRecovery ?: false
        )
    }

    fun refreshAccessToken(refreshToken: String) : String? {
        val extractedEmail = tokenService.extractEmail(refreshToken)
        return extractedEmail?.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email)
            val refreshTokenUseDetails = tokenRefreshRepository.findUserDetailsByToken(refreshToken)

            if(!tokenService.isExpired(refreshToken) && refreshTokenUseDetails?.username == currentUserDetails.username){
                createAccessToken(currentUserDetails)
            } else {
                null
            }
        }
    }

    private fun createAccessToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = getAccessTokenExpiration()
    )
    private fun createRefreshToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = getRefreshTokenExpiration()
    )
    private fun getAccessTokenExpiration() : Date =
        Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration)
    private fun getRefreshTokenExpiration() : Date =
        Date(System.currentTimeMillis()+jwtProperties.refreshTokenExpiration)
}