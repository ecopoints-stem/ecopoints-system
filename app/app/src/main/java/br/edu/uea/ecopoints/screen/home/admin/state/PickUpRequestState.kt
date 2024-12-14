package br.edu.uea.ecopoints.screen.home.admin.state

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails

interface PickUpRequestState {
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

    data object Success : PickUpRequestState {
        override val isSuccess: Boolean = true
    }

    data object Loading : PickUpRequestState {
        override val isProgressVisible: Boolean = true
    }

    data class Failed(
        override val errorMessage: String,
        override val errorDetails: ExceptionDetails?
    ) : PickUpRequestState
}