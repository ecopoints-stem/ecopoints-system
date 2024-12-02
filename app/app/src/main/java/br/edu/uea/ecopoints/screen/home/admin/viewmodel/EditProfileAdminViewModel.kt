package br.edu.uea.ecopoints.screen.home.admin.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.domain.network.request.AdminUpdate
import br.edu.uea.ecopoints.screen.home.admin.state.EditProfileAdminState
import br.edu.uea.ecopoints.screen.state.register.AdminRegisterState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditProfileAdminViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<EditProfileAdminState>()
    val state: LiveData<EditProfileAdminState> = _state

    private val _admin = MutableLiveData<CoopAdmin?>(null)
    val admin: LiveData<CoopAdmin?> = _admin

    fun updateAdmin(name: String, email: String, phone: String?, password: String) {
        viewModelScope.launch (Dispatchers.IO){
            _state.postValue(EditProfileAdminState.Loading)
            _state.postValue(
                runCatching {
                    val adminId: Long = shared.getLong("id", -1L)
                    ecoApi.updateAdmin(adminId,
                        AdminUpdate(name,email,phone,password)
                    )
                }.fold(
                    onFailure = { error ->
                        EditProfileAdminState.Failed(error.message ?: "Erro No EditProfile", null)
                    }, onSuccess = { response: Response<CoopAdmin> ->
                        if(response.isSuccessful){
                            _admin.postValue(response.body())
                            Log.i("ECO","updated response ${response.body()}")
                            response.body()?.let { adm ->
                                with(shared.edit()){
                                    putString("email",adm.email)
                                    putString("password",password)
                                    commit()
                                }
                            }
                            EditProfileAdminState.Success(response.body())
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("ECO","errorBody $errorBody")
                            errorBody?.let {  apiError ->
                                try {
                                    val exceptionDetails = mapper.readValue(apiError,
                                        ExceptionDetails::class.java)
                                    EditProfileAdminState.Failed(exceptionDetails.title,exceptionDetails)
                                } catch (ex: Exception){
                                    Log.e("ECO",ex.message ?: "Erro ao deserializar API response",null)
                                    EditProfileAdminState.Failed("Erro ao deserializar API response",null)
                                }
                            }
                        }
                    }
                )
            )
        }
    }

    fun getAdminById() {
        viewModelScope.launch (Dispatchers.IO){
            _admin.postValue(
                runCatching {
                    val adminId: Long = shared.getLong("id", -1L)
                    ecoApi.findAdminById(adminId)
                }.fold(
                    onFailure = { error ->
                        _state.postValue(EditProfileAdminState.Failed("Erro ao recuperar informações atuais do seu usuário ${error.message}",null))
                        null
                    },
                    onSuccess = { response ->
                        if(response.isSuccessful){
                            Log.i("ECO","updated response ${response.body()}")
                            response.body()
                        }else{
                            _state.postValue(EditProfileAdminState.Failed("Erro ao recuperar informações atuais do seu usuário ${response.code()}",null))
                            null
                        }
                    }
                )
            )
        }
    }

    fun successAfter(){
        _state.value = EditProfileAdminState.SuccessAfter
    }
}