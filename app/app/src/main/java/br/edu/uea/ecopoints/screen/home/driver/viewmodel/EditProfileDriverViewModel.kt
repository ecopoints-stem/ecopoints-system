package br.edu.uea.ecopoints.screen.home.driver.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import br.edu.uea.ecopoints.data.api.EcoApi
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditProfileDriverViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {

}