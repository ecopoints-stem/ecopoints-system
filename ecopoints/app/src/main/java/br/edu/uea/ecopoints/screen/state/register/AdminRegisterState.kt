package br.edu.uea.ecopoints.screen.state.register

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.CoopAdmin

sealed interface AdminRegisterState {
    val admin: CoopAdmin?
        get() = null
    val isProgressVisible: Boolean
        get() = false
    val errorMessage: String?
        get() = null
    val isErrorMessageVisible: Boolean
        get() = errorMessage!=null
    val errorResponseApi: ExceptionDetails?
        get() = null

    data class Success(
        override val admin: CoopAdmin
    ) : AdminRegisterState

    data class InconsistentInput(
        override val errorResponseApi: ExceptionDetails,
        override val errorMessage: String) : AdminRegisterState

    data object Loading : AdminRegisterState {
        override val isProgressVisible: Boolean = true
    }

    data class Failed(
        override val errorResponseApi: ExceptionDetails?,
        override val errorMessage: String
    ) : AdminRegisterState
}