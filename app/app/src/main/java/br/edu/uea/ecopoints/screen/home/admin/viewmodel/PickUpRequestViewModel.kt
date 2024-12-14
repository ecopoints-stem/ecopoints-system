package br.edu.uea.ecopoints.screen.home.admin.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.screen.home.admin.fragment.recyclerview.PickUpRequestPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PickUpRequestViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences
) : ViewModel() {
    val pickups = Pager(PagingConfig(pageSize = 3)){
        PickUpRequestPagingSource(ecoApi, shared.getLong("id",-1L))
    }.flow.cachedIn(viewModelScope)
}