package br.edu.uea.ecopoints.screen.home.admin.viewmodel.report

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.network.response.BarData
import br.edu.uea.ecopoints.screen.home.admin.state.report.DefaultState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ControlViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<DefaultState>()
    val state: LiveData<DefaultState> = _state

    private val _barData = MutableLiveData<BarData?>(null)
    val barData : LiveData<BarData?> = _barData

    fun getBarData(endDate: String) {
        val adminId = shared.getLong("id",-1L)
        viewModelScope.launch (Dispatchers.IO){
            _state.postValue(DefaultState.Loading)
            _state.postValue(
                runCatching {
                   ecoApi.getBarData(adminId, endDate)
                }.fold(
                    onFailure = { error -> DefaultState.Failed(null,error.message ?: "Erro ao buscar gráfico") },
                    onSuccess = { response: Response<BarData> ->
                        if(response.isSuccessful){
                            response.body()?.let { data: BarData ->
                                _barData.postValue(data)
                            }
                            DefaultState.Success(response.body() ?: "OK")
                        } else {
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
                            DefaultState.Failed(errorDetails,"Servidor deu erro: ${response.code()}")
                        }
                    }
                )
            )
        }
    }

    fun generateControlReport(endDate: String) {
        val adminId = shared.getLong("id",-1L)
        viewModelScope.launch (Dispatchers.IO){
            _state.postValue(DefaultState.Loading)
            _state.postValue(
                runCatching {
                    ecoApi.adminReport(adminId, endDate)
                }.fold(
                    onFailure = { error -> DefaultState.Failed(null,error.message ?: "Erro ao criar relatório") },
                    onSuccess = { response: Response<Unit> ->
                        if(response.isSuccessful){
                            DefaultState.Success("OK")
                        } else {
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
                            DefaultState.Failed(errorDetails,"Servidor deu erro: ${response.code()}")
                        }
                    }
                )
            )
        }
    }
}