package br.edu.uea.ecopoints.config

import br.edu.uea.ecopoints.config.email.MailSenderProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
@EnableConfigurationProperties(MailSenderProperties::class)
class Config (
    private val mailSenderProperties: MailSenderProperties
) {
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
}