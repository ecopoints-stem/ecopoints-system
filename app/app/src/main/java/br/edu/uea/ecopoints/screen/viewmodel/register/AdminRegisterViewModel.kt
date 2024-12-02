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
import br.edu.uea.ecopoints.domain.network.request.AdminRegister
import br.edu.uea.ecopoints.screen.state.register.AdminRegisterState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRegisterViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<AdminRegisterState>()
    val state: LiveData<AdminRegisterState> = _state

    fun save(name: String, phone: String?,
             email: String, password: String,
             securityQuestion: String?, securityResponse: String?,
             cooperativeName: String?, cooperativeCnpj: String?) {

        val adminRegister = AdminRegister(
            name = name,
            phone = phone,
            email = email,
            password = password,
            securityQuestion = securityQuestion,
            securityResponse = securityResponse,
            cooperativeName = cooperativeName,
            cooperativeCnpj = cooperativeCnpj
        )
        Log.i("ECO","Mandando esse json pra la $adminRegister")
        _state.value = AdminRegisterState.Loading
        viewModelScope.launch (Dispatchers.IO){
            val adminResponse = ecoApi.createAdmin(adminRegister)
            if(adminResponse.isSuccessful){
                Log.i("ECO","Requisição de criação de admin bem sucedida")
                adminResponse.body()?.let { coopAdmin: CoopAdmin ->
                    Log.i("ECO","Admin $coopAdmin")
                    _state.postValue(
                        AdminRegisterState.Success(
                            coopAdmin
                        )
                    )
                }
            } else {
                //Deu erro, precisa mostrar ao usuário
                val errorBody = adminResponse.errorBody()?.string()
                Log.e("ECO","errorBody $errorBody")
                errorBody?.let { apiErrorBody ->
                    try {
                        val exceptionDetails = mapper.readValue(apiErrorBody,ExceptionDetails::class.java)
                        Log.i("ECO", "Exceptions Details $exceptionDetails")
                        _state.postValue(AdminRegisterState.InconsistentInput(exceptionDetails,"Erro na API code ${adminResponse.code()}"))
                    } catch (ex: Exception){
                        Log.e("ECO",ex.message ?: "Erro em deserializar")
                        _state.postValue(AdminRegisterState.Failed(null,"Erro na API code ${adminResponse.code()}"))
                    }
                }
            }
        }
    }
}