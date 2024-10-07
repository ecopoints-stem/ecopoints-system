package br.edu.uea.ecopoints.controller

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.config.security.authentication.request.LoginRequest
import br.edu.uea.ecopoints.config.security.authentication.response.LoginResponse
import br.edu.uea.ecopoints.config.security.authentication.service.AuthenticationService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Resource")
class AuthResource (
    private val authService: AuthenticationService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService
) {
    @PostMapping
    fun authenticate(
        @RequestBody @Valid authRequest: LoginRequest
    ) : ResponseEntity<LoginResponse> {
        val authenticationResponse = authService.authentication(authRequest)
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse)
    }
}