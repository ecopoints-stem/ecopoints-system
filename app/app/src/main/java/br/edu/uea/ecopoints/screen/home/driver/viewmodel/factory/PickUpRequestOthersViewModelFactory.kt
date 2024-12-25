package br.edu.uea.ecopoints.screen.home.driver.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.PickUpRequestOthersViewModel

class PickUpRequestOthersViewModelFactory(
    private val ecoApi: EcoApi,
    private val driverId: Long,
    private val statuses: List<String>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass == PickUpRequestOthersViewModel::class.java){
            return PickUpRequestOthersViewModel(ecoApi, driverId, statuses) as T
        }
        throw IllegalArgumentException("Unknown ViewModel instance for $modelClass")
    }
}