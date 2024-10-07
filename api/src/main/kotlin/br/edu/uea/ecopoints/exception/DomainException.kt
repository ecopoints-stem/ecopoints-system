package br.edu.uea.ecopoints.exception

import br.edu.uea.ecopoints.enums.ExceptionDetailsStatus

data class DomainException(
    override val message: String?,
    val type: ExceptionDetailsStatus
) : RuntimeException(message)
