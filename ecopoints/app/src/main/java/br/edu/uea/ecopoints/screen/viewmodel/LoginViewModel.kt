package br.edu.uea.ecopoints.screen.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.domain.network.request.UserLogin
import br.edu.uea.ecopoints.screen.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val ecoApi: EcoApi,
    private val shared: SharedPreferences
) : ViewModel()  {
    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState> = _state

    fun authenticate(email: String, password: String) {
        _state.value = LoginState.Loading
        val userInfo = UserLogin(email, password)
        viewModelScope.launch{
            _state.value = runCatching {
                ecoApi.login(userInfo)
            }.fold(onSuccess = {
                response ->
                    when{
                        response.isSuccessful -> {
                            with(shared.edit()){
                                response.body()?.let {
                                    putLong("id",it.id)
                                    putString("role",it.role?.substringAfter("ROLE_"))
                                    putString("accessToken",it.accessToken)
                                    putString("refreshToken",it.refreshToken)
                                    putString("email",email)
                                    putString("password",password)
                                }
                                commit()
                            }
                            LoginState.Success(response.body(), true)
                        }
                        response.code() == 403 -> {
                            LoginState.Failed("Senha ou email inválidos")
                        }
                        else ->
                            LoginState.Failed("Erro na API ${response.code()}")
                    }
            }, onFailure = { error ->
                LoginState.Failed(error.message ?: "Erro ao executar retrofit")
            })
        }
    }
}