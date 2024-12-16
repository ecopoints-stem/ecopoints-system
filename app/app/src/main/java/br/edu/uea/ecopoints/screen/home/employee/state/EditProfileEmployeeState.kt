package br.edu.uea.ecopoints.screen.home.employee.state

import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Employee

sealed interface EditProfileEmployeeState {
    val employeeUpdated: Employee?
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
        override val employeeUpdated: Employee?
    ) : EditProfileEmployeeState

    data object Loading : EditProfileEmployeeState {
        override val isProgressVisible: Boolean = true
    }

    data object SuccessAfter: EditProfileEmployeeState {
        override val employeeUpdated: Employee? = null
    }

    data class Failed(
        override val errorMessage: String,
        override val errorDetails: ExceptionDetails?
    ) : EditProfileEmployeeState
}