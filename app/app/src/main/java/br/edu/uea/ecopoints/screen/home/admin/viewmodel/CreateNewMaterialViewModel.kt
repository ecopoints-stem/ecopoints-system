package br.edu.uea.ecopoints.screen.home.admin.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Material
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import br.edu.uea.ecopoints.screen.state.register.AdminRegisterState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CreateNewMaterialViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel(){
    private var _isLoadingVisible : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isLoadingVisible: LiveData<Boolean> = _isLoadingVisible
    private var _isErrorMessageVisible: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val isErrorMessageVisible : LiveData<Boolean> = _isErrorMessageVisible
    private var _errorMessage : MutableLiveData<String?> = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage
    private var _resultApi : MutableLiveData<Any?> = MutableLiveData<Any?>(null)
    val resultApi: LiveData<Any?> = _resultApi
    private var _successMessage: MutableLiveData<String?> = MutableLiveData<String?>(null)
    val successMessage: LiveData<String?> = _successMessage

    fun saveMaterial(materialId: Long? = null, materialName: String, materialType: MaterialType) {
        _isLoadingVisible.value = true
        val material = Material(materialId, materialName, materialType)
        viewModelScope.launch (Dispatchers.IO) {
            _resultApi.postValue(runCatching {
                val userId = shared.getLong("id",-1L)
                ecoApi.addNewMaterialForCooperative(userId,material)
            }.fold(
                onFailure = { error ->
                    _isLoadingVisible.postValue(false)
                    _isErrorMessageVisible.postValue(true)
                    _errorMessage.postValue(error.message)
                }, onSuccess = {
                    response: Response<List<Material>> ->
                        if(response.isSuccessful){
                            _isLoadingVisible.postValue(false)
                            _isErrorMessageVisible.postValue(false)
                            _resultApi.postValue(response.body())
                            _successMessage.postValue(
                                response.body()
                                    ?.sortedBy { it.name }
                                    ?.joinToString(separator = "\n") {
                                        "NOME: ${it.name} TIPO: ${it.type}"
                                    }
                            )
                        } else{
                            _isLoadingVisible.postValue(false)
                            _isErrorMessageVisible.postValue(true)
                            // Pegar o exception details
                            val errorBody = response.errorBody()?.string()
                            Log.e("ECO","errorBody $errorBody")
                            errorBody?.let { apiErrorBody ->
                                try {
                                    val exceptionDetails = mapper.readValue(apiErrorBody,
                                        ExceptionDetails::class.java)
                                    Log.i("ECO", "Exceptions Details $exceptionDetails")
                                    _resultApi.postValue(exceptionDetails)
                                } catch (ex: Exception){
                                    Log.e("ECO",ex.message ?: "Erro em deserializar")
                                    _errorMessage.postValue("Erro na API code ${response.code()}")
                                }
                            }
                        }
                }
            ))
        }
    }

    fun successAfter(){
        _successMessage.value = null
    }
}