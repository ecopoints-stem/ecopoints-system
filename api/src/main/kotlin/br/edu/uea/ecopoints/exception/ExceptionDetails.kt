package br.edu.uea.ecopoints.exception

import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus
import java.time.LocalDateTime

data class ExceptionDetails (
    val title: String,
    val status: ExceptionDetailsStatus,
    val timestamp: LocalDateTime,
    val exception: String,
    val details: MutableMap<String, String?>
)