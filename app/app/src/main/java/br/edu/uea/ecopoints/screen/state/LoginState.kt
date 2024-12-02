package br.edu.uea.ecopoints.screen.state

import br.edu.uea.ecopoints.domain.network.response.UserLoginTokens
import br.edu.uea.ecopoints.screen.LoginActivity

sealed interface LoginState {
    val auth: UserLoginTokens?
        get() = null
    val isProgressVisible: Boolean
        get() = false
    val errorMessage: String?
        get() = null
    val isErrorMessageVisible: Boolean
        get() = errorMessage!=null
    val isAuthenticated: Boolean
        get() = false

    data class Success(
        override val auth: UserLoginTokens?,
        override val isAuthenticated: Boolean
    ) : LoginState

    data object Loading : LoginState{
        override val isProgressVisible: Boolean = true
    }

    data class Failed(
        override val errorMessage: String
    ) : LoginState
}