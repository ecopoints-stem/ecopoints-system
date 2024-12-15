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
            
            Aqui o relatório geral de administrador da cooperativa solicitado
            
            Atenciosamente,
            TI - Academia STEM, Pilar Excelência
        """.trimIndent()
    }
}