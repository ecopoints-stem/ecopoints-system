package br.edu.uea.ecopoints.screen.home.driver.viewmodel.factory

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.PickUpRequestTodayViewModel
import com.fasterxml.jackson.databind.ObjectMapper

class PickUpRequestTodayViewModelFactory(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper,
    private val driverId: Long,
    private val pickDate: String,
    private val statuses: List<String>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass == PickUpRequestTodayViewModel::class.java){
            return PickUpRequestTodayViewModel(ecoApi, shared, mapper, driverId, pickDate, statuses) as T
        }
        throw IllegalArgumentException("Unknown ViewModel instance for $modelClass")
    }
}