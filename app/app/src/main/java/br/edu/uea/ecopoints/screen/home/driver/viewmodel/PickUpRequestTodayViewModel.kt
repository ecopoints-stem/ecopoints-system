package br.edu.uea.ecopoints.screen.home.driver.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.enums.PickupRequestStatus
import br.edu.uea.ecopoints.screen.home.driver.fragment.recyclerview.today.PickUpRequestTodayPagingSource
import com.fasterxml.jackson.databind.ObjectMapper

class PickUpRequestTodayViewModel (
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper,
    private val driverId: Long,
    private val pickDate: String,
    private val statuses: List<String>
) : ViewModel() {
    val pickups = Pager(PagingConfig(pageSize = 3)){
        PickUpRequestTodayPagingSource(ecoApi,driverId, pickDate, statuses)
    }.flow.cachedIn(viewModelScope)

    suspend fun updatePickUpStatus(pickUpId: Long, newStatus: PickupRequestStatus): PickUpRequest? {
        val response = ecoApi.updatePickUpStatus(pickUpId, newStatus)
        return if(response.isSuccessful) response.body() else null
    }
}