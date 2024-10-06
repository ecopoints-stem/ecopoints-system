package br.edu.uea.ecopoints.config.security.authentication.filter

import br.edu.uea.ecopoints.config.security.authentication.service.TokenService
import br.edu.uea.ecopoints.config.security.userdetails.CustomUserDetailsService
import io.jsonwebtoken.ExpiredJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter (
    private val userDetails: CustomUserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header : String? = request.getHeader("Authorization")
        if(header.doesNotContainBearerToken() || request.requestURL.contains("auth")){
            filterChain.doFilter(request,response)
            return
        }
        try {
            val jwtToken = header!!.extractTokenValue()
            val email = tokenService.extractEmail(jwtToken)
            if(email!=null && SecurityContextHolder.getContext().authentication == null){
                val foundUser = userDetails.loadUserByUsername(email)
                if(tokenService.isValid(jwtToken,foundUser)){
                    val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
                filterChain.doFilter(request,response)
            }
        } catch (ex: ExpiredJwtException){
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Token Expirado")
            return
        }
    }

    private fun String?.doesNotContainBearerToken() =
        this==null || !this.startsWith("Bearer ")
    private fun String.extractTokenValue() = this.substringAfter("Bearer ")
}