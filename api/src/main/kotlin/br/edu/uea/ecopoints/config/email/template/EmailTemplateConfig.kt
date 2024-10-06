package br.edu.uea.ecopoints.config.email.template

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage

@Configuration
class EmailTemplateConfig {
    @Bean("welcomeMessageTemplate")
    fun welcomeMessageTemplate() : SimpleMailMessage {
        val welcomeTemplate = SimpleMailMessage()
        welcomeTemplate.subject = "Bem vindo ao sistema Ecopoints !!!"
        welcomeTemplate.text = """
            Olá %s,
            
            Seja muito bem vindo ao Sistema Ecopoints, o aplicativo de gerenciamento de resíduos recicláveis da Academia STEM.
            De acordo com seu cadastro, o seu papel é de %s. Aproveite os nossos serviços !
        """.trimIndent()
        return welcomeTemplate
    }

    @Bean("resetPasswordMessageTemplate")
    fun resetPasswordMessageTemplate() : SimpleMailMessage {
        val resetPasswordTemplate = SimpleMailMessage()
        resetPasswordTemplate.subject = "Recuperação de senha"
        resetPasswordTemplate.text = """
            Olá %s,
            
            Vimos que foi solicitado a recuperação de senha pelo aplicativo. Os procedimentos para a configuração da nova senha serão os seguintes:
            
            1) Insira a senha temporária gerada automaticamente pelo sistema
                - SENHA: %s
            2) Na Tela seguinte, insira a nova senha e confirme sua senha.
            3) Por fim, você irá novamente fazer login no aplicativo, porém agora com a senha alterada.
        """.trimIndent()
        return resetPasswordTemplate
    }
}