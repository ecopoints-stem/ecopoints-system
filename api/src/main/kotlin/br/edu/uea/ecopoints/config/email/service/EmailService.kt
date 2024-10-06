package br.edu.uea.ecopoints.config.email.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService (
    private val emailSender: JavaMailSender,
    @Qualifier("welcomeMessageTemplate")
    private val welcomeTemplate: SimpleMailMessage,
    @Qualifier("resetPasswordMessageTemplate")
    private val resetPasswordTemplate: SimpleMailMessage
) {
    fun sendWelcomeMessage(targetEmail: String, name: String, role: String){
        val welcomeMessage = SimpleMailMessage(welcomeTemplate)
        val text = welcomeTemplate.text
        welcomeMessage.text=text!!.format(name,role)
        welcomeMessage.setTo(targetEmail)
        emailSender.send(welcomeMessage)
    }

    fun sendResetPasswordMessage(targetEmail: String, name: String, password : String){
        val resetPasswordMessage = SimpleMailMessage(resetPasswordTemplate)
        val text = resetPasswordTemplate.text
        resetPasswordMessage.text=text!!.format(name,password)
        resetPasswordMessage.setTo(targetEmail)
        emailSender.send(resetPasswordMessage)
    }
}