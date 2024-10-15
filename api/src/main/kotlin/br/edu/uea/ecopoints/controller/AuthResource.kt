package br.edu.uea.ecopoints.controller

import br.edu.uea.ecopoints.config.email.service.EmailService
import br.edu.uea.ecopoints.config.security.authentication.request.LoginRequest
import br.edu.uea.ecopoints.config.security.authentication.request.RefreshTokenRequest
import br.edu.uea.ecopoints.config.security.authentication.request.ResetPasswordRequest
import br.edu.uea.ecopoints.config.security.authentication.response.LoginResponse
import br.edu.uea.ecopoints.config.security.authentication.response.RefreshTokenResponse
import br.edu.uea.ecopoints.config.security.authentication.service.AuthenticationService
import br.edu.uea.ecopoints.domain.user.CooperativeAdministrator
import br.edu.uea.ecopoints.domain.user.RecyclingSorter
import br.edu.uea.ecopoints.domain.user.model.EcoUser
import br.edu.uea.ecopoints.dto.user.*
import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import br.edu.uea.ecopoints.exception.DomainException
import br.edu.uea.ecopoints.exception.ExceptionDetails
import br.edu.uea.ecopoints.service.cooperative.ICooperativeService
import br.edu.uea.ecopoints.service.user.ICoopAdmService
import br.edu.uea.ecopoints.service.user.IDriverService
import br.edu.uea.ecopoints.service.user.IRecyclingSorterService
import br.edu.uea.ecopoints.service.user.model.IUserService
import br.edu.uea.ecopoints.utils.PasswordGenerator
import br.edu.uea.ecopoints.view.user.CoopAdmView
import br.edu.uea.ecopoints.view.user.DriverView
import br.edu.uea.ecopoints.view.user.RecyclingSorterView
import br.edu.uea.ecopoints.view.user.UserView
import io.jsonwebtoken.ExpiredJwtException
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import kotlin.concurrent.thread

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Resource")
class AuthResource (
    private val authService: AuthenticationService,
    private val encoder: PasswordEncoder,
    private val emailService: EmailService,
    private val userService: IUserService,
    private val coopAdmService: ICoopAdmService,
    private val recyclingSorterService: IRecyclingSorterService,
    private val driverService: IDriverService,
    private val cooperativeService: ICooperativeService,
) {
    @PostMapping
    fun authenticate(
        @RequestBody @Valid authRequest: LoginRequest
    ) : ResponseEntity<LoginResponse> {
        val authenticationResponse = authService.authentication(authRequest)
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody @Valid authRefreshRequest: RefreshTokenRequest) : ResponseEntity<Any> {
        var tokenResponse: RefreshTokenResponse? = null
        try {
            authService.refreshAccessToken(refreshToken = authRefreshRequest.refreshToken)?.let { newAccessToken ->
                tokenResponse = RefreshTokenResponse(newAccessToken)
            }
        } catch (ex: ExpiredJwtException){
            val responseException = ExceptionDetails(
                exception = ex.javaClass.toString(),
                title = "Token de refresh expirou",
                status = ExceptionDetailsStatus.TOKEN_EXPIRED,
                timestamp = LocalDateTime.now(),
                details = mutableMapOf(
                    (ex.cause?.message ?: "expirou access token") to ex.message
                )
            )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseException)
        }

        return ResponseEntity.status(HttpStatus.OK).body(tokenResponse)
    }

    @PostMapping("/resetPassword/{userId}")
    fun resetPassword(@PathVariable userId: Long) : ResponseEntity<UserResetPasswordInfo>{
        val newPassword = PasswordGenerator.generateNewPassword()
        val user = userService.findById(userId)
        if(!user.isPasswordRecovery){
            val userUpdated = userService.resetPassword(user, newPassword)
            thread(true){
                emailService.sendResetPasswordMessage(
                    userUpdated.email,userUpdated.name,newPassword
                )
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(UserResetPasswordInfo("Senha temporária enviada para o email ${user.email}"))
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

    @PostMapping("/admin")
    fun saveAdm(@RequestBody @Valid coopAdmRegister: CoopAdmRegister) : ResponseEntity<CoopAdmView> {
        val coopAdm = coopAdmRegister.toEntity()
        coopAdm.password = encoder.encode(coopAdm.password)
        var coopAdmSaved : CooperativeAdministrator? = null
        if(coopAdmRegister.cooperativeCnpj!=null){
            if(cooperativeService.existsByCpnj(coopAdmRegister.cooperativeCnpj)){
                val cooperative = cooperativeService.findByCnpj(coopAdmRegister.cooperativeCnpj)
                coopAdm.cooperative = cooperative
                coopAdmSaved = coopAdmService.save(coopAdm)
                cooperative.adm = coopAdmSaved
                cooperativeService.save(cooperative)
            }
        } else {
            coopAdmSaved = coopAdmService.save(coopAdm)
        }
        coopAdmSaved?.let { adm ->
            thread(true){
                emailService.sendWelcomeMessage(
                    adm.email,
                    adm.name,
                    adm.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(coopAdmSaved?.toAView())
    }

    @PostMapping("/employee")
    fun saveEmployee(@RequestBody @Valid recyclingSorterRegister: RecyclingSorterRegister)  : ResponseEntity<RecyclingSorterView> {
        val recyclingSorter = recyclingSorterRegister.toEntity()
        recyclingSorter.password = encoder.encode(recyclingSorter.password)
        var recyclingSorterSaved: RecyclingSorter? = null
        if(recyclingSorterRegister.cnpj!=null){
            if(cooperativeService.existsByCpnj(recyclingSorterRegister.cnpj)) {
                val cooperative = cooperativeService.findByCnpj(recyclingSorterRegister.cnpj)
                recyclingSorter.cooperative = cooperative
                recyclingSorterSaved = recyclingSorterService.save(recyclingSorter)
                cooperative.employees.add(recyclingSorterSaved)
                cooperativeService.save(cooperative)
            }
        } else {
            recyclingSorterSaved = recyclingSorterService.save(recyclingSorter)
        }
        recyclingSorterSaved?.let { employee ->
            thread(true){
                emailService.sendWelcomeMessage(
                    employee.email,
                    employee.name,
                    employee.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(recyclingSorterSaved?.toRView())
    }

    @PostMapping("/driver")
    fun saveDriver(@RequestBody @Valid driverDTO: DriverRegister) : ResponseEntity<DriverView> {
        val driver = driverDTO.toEntity()
        driver.password = encoder.encode(driver.password)
        val driverSaved = driverService.save(driver)
        driverSaved.let {
            thread(true){
                emailService.sendWelcomeMessage(
                    it.email,
                    it.name,
                    it.role.toString().substringAfter("ROLE_")
                )
            }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(driverSaved.toDView())
    }

    @GetMapping("/userId/{email}")
    fun getUserIdByEmail(@PathVariable("email") email: String) : ResponseEntity<UserInfo>{
        val info = UserInfo(userService.findUserIdByEmail(email))
        return ResponseEntity.status(HttpStatus.OK).body(info)
    }
}