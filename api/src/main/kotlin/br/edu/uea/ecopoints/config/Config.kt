package br.edu.uea.ecopoints.config

import br.edu.uea.ecopoints.config.email.MailSenderProperties
import br.edu.uea.ecopoints.config.security.authentication.filter.JwtAuthenticationFilter
import br.edu.uea.ecopoints.config.security.authentication.jwt.JwtProperties
import br.edu.uea.ecopoints.config.security.userdetails.CustomUserDetailsService
import br.edu.uea.ecopoints.repository.user.model.EcoUserRepository
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.security.SecuritySchemes
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableConfigurationProperties(MailSenderProperties::class, JwtProperties::class)
@EnableWebSecurity
@SecuritySchemes(
    SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
    )
)
class Config (
    private val mailSenderProperties: MailSenderProperties
) : OpenApiCustomizer {
    @Bean
    fun javaMailSender() : JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailSenderProperties.host
        mailSender.port = mailSenderProperties.port
        mailSender.username = mailSenderProperties.username
        mailSender.password = mailSenderProperties.password


        mailSender.javaMailProperties["mail.transport.protocol"] = mailSenderProperties.protocol
        mailSender.javaMailProperties["mail.smtp.auth"] = mailSenderProperties.auth
        mailSender.javaMailProperties["mail.smtp.starttls.enable"] = mailSenderProperties.starttlsEnable
        mailSender.javaMailProperties["mail.debug"] = mailSenderProperties.debug

        return mailSender
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        authFilter: JwtAuthenticationFilter
    ) : DefaultSecurityFilterChain {
        http.csrf{ csrf -> csrf.disable()}.authorizeHttpRequests {
            auth ->
                auth.requestMatchers(
                    "/swagger-ui/**", "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/v3/api-docs/**"
                ).permitAll()
                auth.requestMatchers("/admin").hasRole("ADMINISTRATOR")
                auth.requestMatchers("/employee").hasAnyRole("ADMINISTRATOR", "EMPLOYEE")
                auth.requestMatchers("/driver").hasAnyRole("ADMINISTRATOR", "DRIVER")
                auth.requestMatchers( "/auth/**").permitAll()
                auth.anyRequest().authenticated()
        }.sessionManagement {
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }.addFilterBefore(authFilter,UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder ) : AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setPasswordEncoder(passwordEncoder)
        authenticationProvider.setUserDetailsService(userDetailsService)
        val providerManager = ProviderManager(authenticationProvider)
        //providerManager.isEraseCredentialsAfterAuthentication = false
        return providerManager
    }

    @Bean
    fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(userRepository: EcoUserRepository) : UserDetailsService = CustomUserDetailsService(userRepository)

    override fun customise(openApi: OpenAPI?) {
        val info: Info = Info()
        info.apply {
            contact = Contact()
                .name("Ecopoints")
                .email("ecopoints.company@gmail.com")
                .url("https://github.com/ecopoints-stem/")

            title="ACADEMIA STEM - PILAR EXCELÊNCIA"
            description="""
                Sistema de gerenciamento de resíduos recicláveis
            """.trimIndent()
        }
        openApi?.info = info
        openApi?.addSecurityItem(
            SecurityRequirement().addList("Bearer Authentication")
        )
    }
}