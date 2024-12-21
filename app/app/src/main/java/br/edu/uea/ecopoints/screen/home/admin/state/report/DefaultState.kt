package br.edu.uea.ecopoints.screen.home.admin.state.report

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails

interface DefaultState {
    val resource: Any?
        get() = null
    val isProgressVisible: Boolean
        get() = false
    val errorMessage: String?
        get() = null
    val isErrorMessageVisible: Boolean
        get() = errorMessage!= null
    val errorResponseApi: ExceptionDetails?
        get() = null

    data class Success(
        override val resource: Any
    ) : DefaultState

    data class InconsistentInput(
        override val errorResponseApi: ExceptionDetails,
        override val errorMessage: String
    ) : DefaultState

    data object Loading : DefaultState {
        override val isProgressVisible = true
    }

    data object NotLoading : DefaultState {
        override val isProgressVisible : Boolean = false
    }

    data class Failed(
        override val errorResponseApi: ExceptionDetails?,
        override val errorMessage: String
    ) : DefaultState
}