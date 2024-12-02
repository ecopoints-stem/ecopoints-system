package br.edu.uea.ecopoints.screen.resetpassword

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.data.api.EcoApi
import br.edu.uea.ecopoints.databinding.ActivityResetPasswordBinding
import br.edu.uea.ecopoints.domain.network.request.ResetPasswordRequest
import br.edu.uea.ecopoints.screen.LoginActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {
    private val binding: ActivityResetPasswordBinding by lazy { ActivityResetPasswordBinding.inflate(layoutInflater) }

    private lateinit var imageViewRoleIcon: ImageView
    private lateinit var edtNewPassword: TextInputEditText
    private lateinit var edtNewPasswordConfirm: TextInputEditText
    private lateinit var textMessage: TextView
    private lateinit var btConfirm: MaterialButton

    @Inject lateinit var ecoApi: EcoApi
    @Inject lateinit var shared: SharedPreferences

    private lateinit var temporaryPassword: String
    private lateinit var userRole: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListeners()
        temporaryPassword = intent.getStringExtra("tmp") ?: ""
        userRole = intent.getStringExtra("roleSelected") ?: ""
        if(userRole.isNotBlank()){
            when(userRole){
                "employee" -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.worker_icon))
                "admin" -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.admin_icon))
                "driver" -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.driver_icon))
                else -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.not_found_icon))
            }
        }
        Log.i("ECO","Temporary: $temporaryPassword User Role: $userRole")
    }

    private fun setupListeners() {
        btConfirm.setOnClickListener {
            if(edtNewPassword.text.isNullOrBlank() || edtNewPasswordConfirm.text.isNullOrBlank()){
                Toast.makeText(this,"Campo de senha ou confirmação vazio",Toast.LENGTH_SHORT).show()
            } else if(edtNewPassword.text.toString()!=edtNewPasswordConfirm.text.toString()){
                textMessage.isVisible = true
                textMessage.text = "Senhas não coincidem"
            } else {
                //Os dados estão válidos, hora de enviar pra API
                lifecycleScope.launch(Dispatchers.IO) {
                    val userId: Long = shared.getLong("id",0L)
                    val passwordRequest: ResetPasswordRequest = ResetPasswordRequest(edtNewPassword.text.toString(),temporaryPassword)
                    Log.i("ECO",passwordRequest.toString())
                    val createNewPasswordResponse = ecoApi.createNewPassword(userId, passwordRequest)
                    if(createNewPasswordResponse.isSuccessful){
                        withContext(Dispatchers.Main){
                            createNewPasswordResponse.body()?.let {
                                Toast.makeText(this@ResetPasswordActivity, "Senha modificada com sucesso ${it.name}",Toast.LENGTH_SHORT).show()
                                val loginIntent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                loginIntent.putExtra("roleSelected",userRole)
                                loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(loginIntent)
                            }
                        }
                    } else{
                        withContext(Dispatchers.Main){
                            textMessage.isVisible = true
                            textMessage.text = "Erra na solicitação"
                        }
                    }
                }
            }
        }
    }

    private fun setupView() {
        imageViewRoleIcon = binding.ivRole
        edtNewPassword = binding.edtNewPassword
        edtNewPasswordConfirm = binding.edtNewPasswordConfirm
        textMessage = binding.tvMessage
        btConfirm = binding.btConfirm
    }
}