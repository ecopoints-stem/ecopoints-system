package br.edu.uea.ecopoints.screen.viewmodel.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Employee
import br.edu.uea.ecopoints.domain.network.request.EmployeeRegister
import br.edu.uea.ecopoints.screen.state.register.EmployeeRegisterState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployeeRegisterViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<EmployeeRegisterState>()
    val state: LiveData<EmployeeRegisterState> = _state

    fun save(name: String, email: String,
             password: String,phone: String?,
             cpf: String, cnpj: String?){
        val employeeRegister = EmployeeRegister(
            name = name,
            phone = phone,
            email = email,
            password = password,
            cnpj = cnpj,
            cpf = cpf
        )
        Log.i("ECO","Mandando esse json pra la $employeeRegister")
        _state.value = EmployeeRegisterState.Loading
        viewModelScope.launch (Dispatchers.IO) {
            val employeeResponse = ecoApi.createEmployee(employeeRegister)
            if(employeeResponse.isSuccessful){
                Log.i("ECO","Requisição de criação de driver bem sucedida")
                employeeResponse.body()?.let { employee: Employee ->
                    Log.i("ECO","Employee $employee")
                    _state.postValue(
                        EmployeeRegisterState.Success(
                            employee
                        )
                    )
                }
            } else {
                //Deu erro, precisa mostrar ao usuário
                val errorBody = employeeResponse.errorBody()?.string()
                Log.e("ECO","errorBody $errorBody")
                errorBody?.let { apiErrorBody ->
                    try {
                        val exceptionDetails = mapper.readValue(apiErrorBody, ExceptionDetails::class.java)
                        Log.i("ECO", "Exceptions Details $exceptionDetails")
                        _state.postValue(EmployeeRegisterState.InconsistentInput("Erro na API code ${employeeResponse.code()}",exceptionDetails))
                    } catch (ex: Exception){
                        Log.e("ECO",ex.message ?: "Erro em deserializar")
                        _state.postValue(EmployeeRegisterState.Failed(null,"Erro na API code ${employeeResponse.code()}"))
                    }
                }
            }
        }
    }
}