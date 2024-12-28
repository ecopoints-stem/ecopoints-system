package br.edu.uea.ecopoints.utils

class PasswordGenerator {
    companion object {
        fun generateNewPassword() : String{
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..8).map { allowedChars.random() }.joinToString("")
        }
    }
}

class EmailTexts {
    companion object {
        val EXCEL_ADMIN_REPORT_SUBJECT = """
            Relatório Geral da Cooperativa
        """.trimIndent()

        val EXCEL_ADMIN_REPORT_BODY = """
            Olá,
            
            Leia com atenção !, este é um email automático, por favor não responda esse email pois você não terá retorno.
            Você realizou a solicitação de um relatório geral da sua cooperativa. Segue o anexo enviado no corpo deste email !!
            
            Atenciosamente,
            TI - Academia STEM, Pilar Excelência
        """.trimIndent()

        val EXCEL_CLIENT_REPORT_SUBJECT = """
            Relatório do cliente na cooperativa
        """.trimIndent()

        val EXCEL_CLIENT_REPORT_BODY = """
            Olá,
            
            Leia com atenção !, este é um email automático, por favor não responda esse email pois você não terá retorno.
            Você realizou a solicitação de um relatório com base nos seus pedidos para uma cooperativa. Segue o anexo enviado no corpo deste email !!
            
            Atenciosamente,
            TI - Academia STEM, Pilar Excelência
        """.trimIndent()
    }
}