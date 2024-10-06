package br.edu.uea.ecopoints.config.security.authentication.service

import br.edu.uea.ecopoints.config.security.authentication.jwt.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class TokenService (
    jwtProperties: JwtProperties
) {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(jwtProperties.key.toByteArray())

    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ) : String = Jwts
        .builder()
        .claims()
        .subject(userDetails.username)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(expirationDate)
        .add(additionalClaims).and()
        .signWith(secretKey).compact()

    fun isValid(token: String, userDetails: UserDetails) : Boolean{
        val email = extractEmail(token)
        return userDetails.username == email && !isExpired(token)
    }
    fun extractEmail(token: String) : String? = getAllClaims(token).subject
    fun isExpired(token: String) : Boolean = getAllClaims(token).expiration.before(Date(System.currentTimeMillis()))
    private fun getAllClaims(token: String) : Claims {
        val parser: JwtParser = Jwts.parser().verifyWith(secretKey).build()
        return parser.parseSignedClaims(token).payload
    }
}