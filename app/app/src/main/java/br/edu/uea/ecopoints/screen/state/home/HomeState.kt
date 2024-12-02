package br.edu.uea.ecopoints.screen.state.home

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails

sealed interface HomeState {
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
    ) : HomeState

    data class InconsistentInput(
        override val errorResponseApi: ExceptionDetails,
        override val errorMessage: String
    ) : HomeState

    data object Loading : HomeState {
        override val isProgressVisible = true
    }

    data object NotLoading : HomeState {
        override val isProgressVisible : Boolean = false
    }

    data class Failed(
        override val errorResponseApi: ExceptionDetails?,
        override val errorMessage: String
    ) : HomeState
}