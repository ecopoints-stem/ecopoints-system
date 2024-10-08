package br.edu.uea.ecopoints.controller

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.config.security.authentication.request.LoginRequest
import br.edu.uea.ecopoints.config.security.authentication.request.RefreshTokenRequest
import br.edu.uea.ecopoints.config.security.authentication.request.ResetPasswordRequest
import br.edu.uea.ecopoints.config.security.authentication.response.LoginResponse
import br.edu.uea.ecopoints.config.security.authentication.response.RefreshTokenResponse
import br.edu.uea.ecopoints.config.security.authentication.service.AuthenticationService
import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.service.user.model.IUserService
import br.edu.uea.ecopoints.utils.PasswordGenerator
import br.edu.uea.ecopoints.view.user.UserView
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.concurrent.thread

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Resource")
class AuthResource (
    private val authService: AuthenticationService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService,
    private val userService: IUserService
) {
    @PostMapping
    fun authenticate(
        @RequestBody @Valid authRequest: LoginRequest
    ) : ResponseEntity<LoginResponse> {
        val authenticationResponse = authService.authentication(authRequest)
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody @Valid request: RefreshTokenRequest) : ResponseEntity<RefreshTokenResponse> {
        var tokenResponse: RefreshTokenResponse? = null
        authService.refreshAccessToken(refreshToken = request.refreshToken)?.let { newAccessToken ->
                tokenResponse = RefreshTokenResponse(newAccessToken)
        }
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse)
    }

    @PostMapping("/resetPassword/{userId}")
    fun resetPassword(@PathVariable userId: Long) : ResponseEntity<String>{
        val newPassword = PasswordGenerator.generateNewPassword()
        val user = userService.findById(userId)
        val userUpdated = userService.resetPassword(user, newPassword)
        thread(true){
            emailService.sendResetPasswordMessage(
                userUpdated.email,userUpdated.name,userUpdated.password
            )
        }
        return ResponseEntity.status(HttpStatus.OK).body("Senha temporária enviada para ${userUpdated.email}")
    }

    @PostMapping("/{userId}/newPassword")
    fun sendNewPassword(@RequestBody @Valid resetPasswordRequest: ResetPasswordRequest, @PathVariable userId: Long) : ResponseEntity<UserView>{
        val user = userService.findById(userId)
        var userUpdated: EcoUser? = null
        if(user.isPasswordRecovery && encoder.matches(resetPasswordRequest.temporaryPassword, user.password)){
            user.isPasswordRecovery=false
            user.password=resetPasswordRequest.newPassword
            userUpdated = userService.save(user)
        } else {
            throw DomainException("Senha inválida ou não ativou modo de recuperação de senha",ExceptionDetailsStatus.INVALID_INPUT)
        }
        return ResponseEntity.status(HttpStatus.OK).body(userUpdated.toView())
    }
}