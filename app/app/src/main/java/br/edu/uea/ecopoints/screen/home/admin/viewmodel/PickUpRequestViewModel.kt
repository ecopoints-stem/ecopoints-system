package br.edu.uea.ecopoints.screen.home.admin.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import br.edu.uea.ecopoints.domain.network.request.PickUpRegister
import br.edu.uea.ecopoints.screen.home.admin.fragment.recyclerview.PickUpRequestPagingSource
import br.edu.uea.ecopoints.screen.home.admin.state.PickUpRequestState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PickUpRequestViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    val pickups = Pager(PagingConfig(pageSize = 3)){
        PickUpRequestPagingSource(ecoApi, shared.getLong("id",-1L))
    }.flow.cachedIn(viewModelScope)

    val state = MutableLiveData<PickUpRequestState>()

    fun createNewPickUpRequest(
        cnpj: String, emailDriver: String,
        address: String, materialType: MaterialType,
        quantity: Double, unitPrice: BigDecimal,
        requestDate: LocalDate
    ) {
        val adminId = shared.getLong("id",-1L)
        val pickUpRegister = PickUpRegister(
            clientCnpj = cnpj, emailDriver = emailDriver,
            address = address, materialType = materialType,
            quantity = quantity, unitPrice = unitPrice,
            requestDate = requestDate, cooperativeAdminId = adminId
        )
        viewModelScope.launch (Dispatchers.IO){
            state.postValue(PickUpRequestState.Loading)
            state.postValue(
                runCatching {
                    ecoApi.createPickUpRequest(pickUpRegister)
                }.fold(onFailure = { error ->
                    PickUpRequestState.Failed(error.message ?: "Erro No Pick Up Request", null)
                }, onSuccess = { response ->
                    if(response.isSuccessful){
                        PickUpRequestState.Success
                    }else{
                        val errorBody = response.errorBody()?.string()
                        Log.e("ECO","errorBody $errorBody")
                        errorBody?.let { apiError ->
                            try {
                                val exceptionDetails = mapper.readValue(
                                    apiError,
                                    ExceptionDetails::class.java
                                )
                                PickUpRequestState.Failed(exceptionDetails.title,exceptionDetails)
                            } catch (ex: Exception) {
                                Log.e(
                                    "ECO",
                                    ex.message ?: "Erro ao deserializar API response",
                                    null
                                )
                                PickUpRequestState.Failed("ERRO NA API ${response.code()}", null)
                            }
                        }
                    }
                })
            )
        }
    }
}