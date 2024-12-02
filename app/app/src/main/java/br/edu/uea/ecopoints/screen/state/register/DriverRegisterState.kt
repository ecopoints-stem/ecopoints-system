package br.edu.uea.ecopoints.screen.state.register

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Driver

interface DriverRegisterState {
    val driver: Driver?
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
        override val driver: Driver
    ) : DriverRegisterState

    data class InconsistentInput(
        override val errorMessage: String,
        override val errorResponseApi: ExceptionDetails
    ) : DriverRegisterState

    data object Loading : DriverRegisterState {
        override val isProgressVisible: Boolean = true
    }

    data class Failed(
        override val errorResponseApi: ExceptionDetails?,
        override val errorMessage: String
    ) : DriverRegisterState
}