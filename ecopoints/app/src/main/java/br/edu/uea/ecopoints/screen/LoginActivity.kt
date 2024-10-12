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
        val role: String = intent.getStringExtra("roleSelected") ?: ""
        if(role.isNotBlank()){
            when(role){
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
                if(state.isAuthenticated){
                    startActivity(Intent(this,TestActivity::class.java))
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
    }

    private fun setupView() {
        imageViewRoleIcon = binding.ivRole
        edtEmail = binding.edtEmail
        edtPassword = binding.edtPassword
        clCreateNewAccount = binding.clCreateAccountText
        btLogin = binding.btLogin
    }
}