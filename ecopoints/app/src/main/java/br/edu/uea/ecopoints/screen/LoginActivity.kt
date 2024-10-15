package br.edu.uea.ecopoints.screen

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.databinding.ActivityLoginBinding
import br.edu.uea.ecopoints.domain.network.response.UserId
import br.edu.uea.ecopoints.domain.network.response.UserLoginTokens
import br.edu.uea.ecopoints.screen.home.admin.HomeAdminActivity
import br.edu.uea.ecopoints.screen.home.driver.HomeDriverActivity
import br.edu.uea.ecopoints.screen.home.employee.HomeEmployeeActivity
import br.edu.uea.ecopoints.screen.register.CoopAdminRegisterActivity
import br.edu.uea.ecopoints.screen.register.DriverRegisterActivity
import br.edu.uea.ecopoints.screen.register.EmployeeRegisterActivity
import br.edu.uea.ecopoints.screen.resetpassword.ResetPasswordActivity
import br.edu.uea.ecopoints.screen.viewmodel.LoginViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var textViewResetPassword: TextView

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

        loginViewModel.state.observe(this){
                state ->
            binding.pbCls.isVisible = state.isProgressVisible
            binding.tvErrorMessage.text = state.errorMessage
            binding.tvErrorMessage.isVisible = state.isErrorMessageVisible
            if(state.isAuthenticated && matchesRole(state.auth,userRole) && state.auth?.isPasswordRecovery != true){
                when(userRole){
                    "admin" -> startActivity(Intent(this,HomeAdminActivity::class.java))
                    "driver" -> startActivity(Intent(this,HomeDriverActivity::class.java))
                    "employee" -> startActivity(Intent(this,HomeEmployeeActivity::class.java))
                }
            } else if(state.isAuthenticated && state.auth?.isPasswordRecovery==true){
                val intent : Intent = Intent(this,ResetPasswordActivity::class.java)
                when(userRole){
                    "admin" -> intent.putExtra("roleSelected","admin")
                    "employee" -> intent.putExtra("roleSelected","employee")
                    "driver" -> intent.putExtra("roleSelected","driver")
                    else -> intent.putExtra("roleSelected","")
                }
                startActivity(intent)
            } else if(state.isAuthenticated && !matchesRole(state.auth,userRole)){
                Toast.makeText(this, "Você escolheu o perfil errado", Toast.LENGTH_LONG).show()
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
        loginViewModel.qtdTentatives.observe(this){ qtd ->
            if (qtd>=3){
                textViewResetPassword.isVisible = true
            }
        }
        loginViewModel.passwordRecovery.observe(this) { recovery: UserId? ->
            if (recovery != null) {
                recovery.userId?.let {
                    Log.i("ECO", "Achou um ID e enviou email de recuperação $it")
                    Toast.makeText(this, "Email de recuperação enviado", Toast.LENGTH_LONG).show()
                    binding.tvResetPassword.isVisible = false
                }
                if(recovery.userId==null){
                    Toast.makeText(this,"Houve erro no pedido de recuperação de senha",Toast.LENGTH_SHORT).show()
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
        textViewResetPassword.setOnClickListener {
            if (edtEmail.text.isNullOrBlank()) {
                Toast.makeText(this@LoginActivity, "Sem email informado de recuperação", Toast.LENGTH_SHORT).show()
            } else {
                val email = edtEmail.text.toString()
                lifecycleScope.launch {
                    loginViewModel.resetPassword(email)
                }
            }
        }
    }

    private fun setupView() {
        imageViewRoleIcon = binding.ivRole
        edtEmail = binding.edtEmail
        edtPassword = binding.edtPassword
        clCreateNewAccount = binding.clCreateAccountText
        btLogin = binding.btLogin
        textViewResetPassword = binding.tvResetPassword
    }

    private fun matchesRole(auth: UserLoginTokens?, roleChoicer: String): Boolean {
        val userRoleAPI = auth?.role
        val userRoleChoicer = roleChoicer.uppercase()
        val result = userRoleAPI?.contains(userRoleChoicer)
        Log.i("ECO","api: $userRoleAPI user choice: $userRoleChoicer result: $result")
        return result ?: false
    }
}