package br.edu.uea.ecopoints.screen.home.employee.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.domain.network.request.EmployeeUpdate
import br.edu.uea.ecopoints.screen.home.employee.state.EditProfileEmployeeState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileEmployeeViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<EditProfileEmployeeState>()
    val state : LiveData<EditProfileEmployeeState> = _state

    fun updateEmployee(
        name: String, email: String,
        phone: String?, cnpjCooperative: String?,
        password: String
    ) {

    }
}