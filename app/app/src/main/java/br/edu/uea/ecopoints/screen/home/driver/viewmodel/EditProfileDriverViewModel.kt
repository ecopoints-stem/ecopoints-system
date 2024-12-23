package br.edu.uea.ecopoints.screen.home.driver.viewmodel

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.domain.network.request.DriverUpdate
import br.edu.uea.ecopoints.screen.home.driver.state.EditProfileDriverState
import br.edu.uea.ecopoints.screen.home.employee.state.EditProfileEmployeeState
import com.fasterxml.jackson.databind.ObjectMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EditProfileDriverViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences,
    private val mapper: ObjectMapper
) : ViewModel() {
    private val _state = MutableLiveData<EditProfileDriverState>()
    val state : LiveData<EditProfileDriverState> = _state

    fun updateDriver(
        name: String, email: String,
        phone: String?, cnh : String,
        password: String
    ) {
       viewModelScope.launch (Dispatchers.IO){
           _state.postValue(EditProfileDriverState.Loading)
           _state.postValue(
               runCatching {
                   val driverId : Long = shared.getLong("id",-1L)
                   ecoApi.updateDriver(
                       id = driverId,
                       DriverUpdate(name, phone, email, password, cnh)
                   )
               }.fold(
                   onFailure = { error ->
                       Log.i("ECO","Erro ${error.message}")
                       EditProfileDriverState.Failed(error.message ?: "Erro requisição para API",null)
                   },
                   onSuccess = { response: Response<Driver> ->
                       if(response.isSuccessful){
                           val newDriver = response.body()
                           Log.i("ECO","updated response $newDriver")
                           newDriver?.let { dr ->
                               with(shared.edit()){
                                   putString("email",dr.email)
                                   putString("password",password)
                                   commit()
                               }
                           }
                           EditProfileDriverState.Success(newDriver, true)
                       }else {
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
                           EditProfileDriverState.Failed("Servidor deu erro: ${response.code()}", errorDetails)
                       }
                   }
               )
           )
       }
    }

    fun successAfter(){
        _state.value = EditProfileDriverState.SuccessAfter
    }

    fun getDriverId() {
        viewModelScope.launch (Dispatchers.IO){
            _state.postValue(
                runCatching {
                    val driverId : Long = shared.getLong("id", -1L)
                    ecoApi.findDriverById(driverId)
                }.fold(
                    onFailure = { error -> EditProfileDriverState.Failed(error.message ?: "Erro ao recuperar Driver", null) },
                    onSuccess = { response: Response<Driver> ->
                        if(response.isSuccessful){
                            val driver = response.body()
                            EditProfileDriverState.Success(driver, false)
                        } else{
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
                            EditProfileDriverState.Failed("Servidor deu erro: ${response.code()}", errorDetails)
                        }
                    }
                )
            )
        }
    }
}