package br.edu.uea.ecopoints.screen.home.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val state = MutableLiveData<HomeState>()
}