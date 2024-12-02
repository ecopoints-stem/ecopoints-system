package br.edu.uea.ecopoints.screen.home.admin.state

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.screen.state.LoginState

sealed interface EditProfileAdminState {
    val adminUpdated: CoopAdmin?
        get() = null
    val isProgressVisible: Boolean
        get() = false
    val errorMessage: String?
        get() = null
    val isErrorMessageVisible: Boolean
        get() = errorMessage!= null
    val errorDetails: ExceptionDetails?
        get() = null

    data class Success(
        override val adminUpdated: CoopAdmin?
    ) : EditProfileAdminState

    data object Loading : EditProfileAdminState {
        override val isProgressVisible: Boolean = true
    }

    data object SuccessAfter: EditProfileAdminState{
        override val adminUpdated: CoopAdmin? = null
    }

    data class Failed(
        override val errorMessage: String,
        override val errorDetails: ExceptionDetails?
    ) : EditProfileAdminState
}