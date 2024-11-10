package br.edu.uea.ecopoints.config.email.template

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage

@Configuration
class EmailTemplateConfig {
    @Bean("welcomeMessageTemplate")
    fun welcomeMessageTemplate() : SimpleMailMessage {
        val welcomeTemplate = SimpleMailMessage()
        welcomeTemplate.subject = "Cadastro realizado com sucesso no sistema Ecopoints"
        welcomeTemplate.text = """
            Prezado Colaborador %s,
            
            Leia com atenção !, este é um email automático, por favor não responda esse email pois você não terá retorno.
            Você realizou o cadastro de uma conta para o perfil %s no aplicativo Ecopoints. Seja muito bem vindo ao Sistema Ecopoints, o aplicativo 
            de gerenciamento de resíduos recicláveis da Academia STEM.
            
            Atenciosamente,
            TI - Academia STEM, Pilar Excelência
        """.trimIndent()
        return welcomeTemplate
    }

    @Bean("resetPasswordMessageTemplate")
    fun resetPasswordMessageTemplate() : SimpleMailMessage {
        val resetPasswordTemplate = SimpleMailMessage()
        resetPasswordTemplate.subject = "Alteração de senha"
        resetPasswordTemplate.text = """
            Prezado Colaborador,
            %s
            Você está recebendo uma nova senha para a recuperação da sua conta e novo login.
            Para isso basta copiar a senha abaixo e colar no campo "senha" da sua tela de login, pois o aplicativo irá redirecioná-lo
            para a tela de configuração de nova senha.
            Sua nova senha da conta é: %s
            
            Atenciosamente,
            TI - Academia STEM, Pilar Excelência
        """.trimIndent()
        return resetPasswordTemplate
    }
}