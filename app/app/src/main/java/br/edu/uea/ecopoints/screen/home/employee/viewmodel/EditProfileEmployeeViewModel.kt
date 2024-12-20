package br.edu.uea.ecopoints.screen.home.employee.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Cooperative
import br.edu.uea.ecopoints.domain.entity.Employee
import br.edu.uea.ecopoints.domain.network.request.EmployeeUpdate
import br.edu.uea.ecopoints.screen.home.admin.state.EditProfileAdminState
import br.edu.uea.ecopoints.screen.home.employee.state.EditProfileEmployeeState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
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

    fun successAfter(){
        _state.value = EditProfileEmployeeState.SuccessAfter
    }

    suspend fun getCnpjByCooperativeId(cooperativeId: Long?): String? {
        return cooperativeId?.let { id ->
            withContext(Dispatchers.IO) {
                runCatching {
                    ecoApi.getCooperativeById(id)
                }.getOrNull()?.body()?.cnpj
            }
        }
    }


    fun getEmployeeId() {
        viewModelScope.launch (Dispatchers.IO){
            _state.postValue(
                runCatching {
                    val employeeId : Long = shared.getLong("id", -1L)
                    ecoApi.findEmployeeById(employeeId)
                }.fold(
                    onFailure = { error ->
                        EditProfileEmployeeState.Failed(error.message ?: "Erro ao recuperar Employee", null)
                    }, onSuccess = { response: Response<Employee> ->
                        if(response.isSuccessful){
                            val employee = response.body()
                            EditProfileEmployeeState.Success(employee)
                        } else{
                            val errorBodyString = response.errorBody()?.string()
                            Log.e("ECO","Error body $errorBodyString")
                            val errorDetails = if(errorBodyString!=null){
                                try {
                                    mapper.readValue(errorBodyString, ExceptionDetails::class.java)
                                } catch (ex: Exception){
                                    null
                                }
                            } else {
                                null
                            }
                            EditProfileEmployeeState.Failed("Servidor deu erro: ${response.code()}", errorDetails)
                        }
                    }
                )
            )
        }
    }
}