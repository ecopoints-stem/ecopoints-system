package br.edu.uea.ecopoints.config.security.authentication.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jwt")
class JwtProperties (
    val key: String,
    val accessTokenExpiration: Long,
    val refreshTokenExpiration: Long
)