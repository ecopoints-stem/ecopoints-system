package br.edu.uea.ecopoints.screen

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.databinding.ActivityLoginBinding
import br.edu.uea.ecopoints.domain.network.response.UserLoginTokens
import br.edu.uea.ecopoints.screen.register.CoopAdminRegisterActivity
import br.edu.uea.ecopoints.screen.register.DriverRegisterActivity
import br.edu.uea.ecopoints.screen.register.EmployeeRegisterActivity
import br.edu.uea.ecopoints.screen.viewmodel.LoginViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var imageViewRoleIcon: ImageView
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var clCreateNewAccount: ConstraintLayout
    private lateinit var btLogin: MaterialButton
    private lateinit var userRole: String

    @Inject
    lateinit var ecoApi: EcoApi
    @Inject
    lateinit var shared: SharedPreferences
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        userRole = intent.getStringExtra("roleSelected") ?: ""
        if(userRole.isNotBlank()){
            when(userRole){
                "employee" -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.worker_icon))
                "admin" -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.admin_icon))
                "driver" -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.driver_icon))
                else -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.not_found_icon))
            }
        }
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        loginViewModel.state.observe(this){
            state ->
                binding.pbCls.isVisible = state.isProgressVisible
                binding.tvErrorMessage.text = state.errorMessage
                binding.tvErrorMessage.isVisible = state.isErrorMessageVisible
                if(state.isAuthenticated && matchesRole(state.auth,userRole)){
                    when(userRole){
                        "admin" -> {

                        }
                    }
                }
        }
    }

    private fun setupListeners() {
        btLogin.setOnClickListener {
            //Verificar se os campos estão corretos
            if(edtEmail.text.toString().isBlank()){
                edtEmail.error="Email inválido"
            } else if(edtPassword.text.toString().isBlank() || edtPassword.text.toString().length <7){
                edtPassword.error="Senha inválida ou curta demais"
            } else{
                loginViewModel.authenticate(edtEmail.text.toString(),edtPassword.text.toString())
            }
        }
        clCreateNewAccount.setOnClickListener {
            //Envia para a tela de cadastro de acordo com o perfil de usuário
            when(userRole) {
                "admin" -> startActivity(Intent(this,CoopAdminRegisterActivity::class.java))
                "driver" -> startActivity(Intent(this,DriverRegisterActivity::class.java))
                "employee" -> startActivity(Intent(this,EmployeeRegisterActivity::class.java))
                else -> startActivity(Intent(this,MainActivity::class.java))
            }
        }
    }

    private fun setupView() {
        imageViewRoleIcon = binding.ivRole
        edtEmail = binding.edtEmail
        edtPassword = binding.edtPassword
        clCreateNewAccount = binding.clCreateAccountText
        btLogin = binding.btLogin
    }

    private fun matchesRole(auth: UserLoginTokens?, roleChoicer: String): Boolean {
        val userRoleAPI = auth?.role
        val userRoleChoicer = roleChoicer.uppercase()
        val result = userRoleAPI?.contains(userRoleChoicer)
        Log.i("ECO","api: $userRoleAPI user choice: $userRoleChoicer result: $result")
        return result ?: false
    }
}