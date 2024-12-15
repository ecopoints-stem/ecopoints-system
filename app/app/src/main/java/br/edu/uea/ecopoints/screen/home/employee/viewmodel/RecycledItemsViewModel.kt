package br.edu.uea.ecopoints.screen.home.employee.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.domain.entity.Material
import br.edu.uea.ecopoints.domain.entity.SeparatedMaterial
import br.edu.uea.ecopoints.domain.network.request.SeparatedMaterialRegister
import br.edu.uea.ecopoints.screen.home.employee.fragment.recyclerview.SeparatedMaterialPagingSource
import br.edu.uea.ecopoints.screen.home.employee.state.RecycledItemsState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RecycledItemsViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    val recycledMaterials = Pager(PagingConfig(pageSize = 6)){
        SeparatedMaterialPagingSource(ecoApi, shared.getLong("id",-1L))
    }.flow.cachedIn(viewModelScope)

    val state = MutableLiveData<RecycledItemsState>()

    fun createNewSeparatedMaterial(
        lldate: String,
        materialName: String,
        quantity: Double
    ) {
        val employeeId = shared.getLong("id", -1L)
        state.value = RecycledItemsState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val resultState = runCatching {
                ecoApi.getMaterialStartingWithName(name = materialName)
            }.fold(
                onFailure = { error ->
                    RecycledItemsState.Failed("Erro ao buscar Nome de Material: ${error.message}", null)
                },
                onSuccess = { listResponse: Response<List<Material>> ->
                    if (listResponse.isSuccessful) {
                        val material = listResponse.body()?.firstOrNull()
                        if (material != null) {
                            createMaterialRequest(
                                employeeId = employeeId,
                                materialId = material.id!!,
                                lldate = lldate,
                                quantity = quantity
                            )
                        } else {
                            RecycledItemsState.Failed("Material não encontrado", null)
                        }
                    } else {
                        RecycledItemsState.Failed("Servidor deu erro: ${listResponse.code()}", null)
                    }
                }
            )
            state.postValue(resultState)
        }
    }

    private suspend fun createMaterialRequest(
        employeeId: Long,
        materialId: Long,
        lldate: String,
        quantity: Double
    ): RecycledItemsState {
        return runCatching {
            ecoApi.createNewSeparatedMaterial(
                employeeId,
                SeparatedMaterialRegister(
                    separatedDate = lldate,
                    materialId = materialId,
                    quantity = quantity
                )
            )
        }.fold(
            onFailure = { error ->
                RecycledItemsState.Failed("Erro ao registrar novo material coletado: ${error.message}", null)
            },
            onSuccess = { response: Response<SeparatedMaterial> ->
                if (response.isSuccessful) {
                    RecycledItemsState.Success
                } else {
                    RecycledItemsState.Failed("Servidor deu erro: ${response.code()}", null)
                }
            }
        )
    }
}