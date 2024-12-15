package br.edu.uea.ecopoints.screen.home.employee.state

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails

interface RecycledItemsState {
    val isSuccess: Boolean
        get() = true
    val isProgressVisible: Boolean
        get() = false
    val errorMessage: String?
        get() = null
    val isErrorMessageVisible: Boolean
        get() = errorMessage!= null
    val errorDetails: ExceptionDetails?
        get() = null

    data object Success : RecycledItemsState {
        override val isSuccess: Boolean = true
    }

    data object Loading : RecycledItemsState {
        override val isProgressVisible: Boolean = true
    }

    data class Failed(
        override val errorMessage: String,
        override val errorDetails: ExceptionDetails?
    ) : RecycledItemsState
}