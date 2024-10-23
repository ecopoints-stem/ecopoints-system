package br.edu.uea.ecopoints.screen.viewmodel.register

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.domain.network.request.DriverRegister
import br.edu.uea.ecopoints.screen.state.register.AdminRegisterState
import br.edu.uea.ecopoints.screen.state.register.DriverRegisterState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverRegisterViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<DriverRegisterState>()
    val state: LiveData<DriverRegisterState> = _state

    fun save(name: String, phone: String?,
             email: String, password: String,
             cnh: String, cpf: String) {
        val driverRegister = DriverRegister(
            name = name,
            phone = phone,
            email = email,
            password = password,
            cnh = cnh,
            cpf = cpf
        )
        Log.i("ECO","Mandando esse json pra la $driverRegister")
        _state.value = DriverRegisterState.Loading
        viewModelScope.launch (Dispatchers.IO){
            val driverResponse = ecoApi.createDriver(driverRegister)
            if(driverResponse.isSuccessful){
                Log.i("ECO","Requisição de criação de driver bem sucedida")
                driverResponse.body()?.let { driver: Driver ->
                    Log.i("ECO","Admin $driver")
                    _state.postValue(
                        DriverRegisterState.Success(
                            driver
                        )
                    )
                }
            } else {
                //Deu erro, precisa mostrar ao usuário
                val errorBody = driverResponse.errorBody()?.string()
                Log.e("ECO","errorBody $errorBody")
                errorBody?.let { apiErrorBody ->
                    try {
                        val exceptionDetails = mapper.readValue(apiErrorBody, ExceptionDetails::class.java)
                        Log.i("ECO", "Exceptions Details $exceptionDetails")
                        _state.postValue(DriverRegisterState.InconsistentInput("Erro na API code ${driverResponse.code()}",exceptionDetails))
                    } catch (ex: Exception){
                        Log.e("ECO",ex.message ?: "Erro em deserializar")
                        _state.postValue(DriverRegisterState.Failed(null,"Erro na API code ${driverResponse.code()}"))
                    }
                }
            }
        }
    }
}