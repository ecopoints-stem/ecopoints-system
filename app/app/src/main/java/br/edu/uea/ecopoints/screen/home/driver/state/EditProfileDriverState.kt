package br.edu.uea.ecopoints.screen.home.driver.state

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.domain.entity.Employee

interface EditProfileDriverState {
    val driverUpdated: Driver?
        get() = null
    val isProgressVisible: Boolean
        get() = false
    val errorMessage: String?
        get() = null
    val isErrorMessageVisible: Boolean
        get() = errorMessage!= null
    val errorDetails: ExceptionDetails?
        get() = null
    val first : Boolean
        get() = false

    data class Success(
        override val driverUpdated: Driver?,
        override val first: Boolean
    ) : EditProfileDriverState

    data object Loading : EditProfileDriverState {
        override val isProgressVisible: Boolean = true
    }

    data object SuccessAfter: EditProfileDriverState {
        override val driverUpdated: Driver? = null
    }

    data class Failed(
        override val errorMessage: String,
        override val errorDetails: ExceptionDetails?
    ) : EditProfileDriverState
}