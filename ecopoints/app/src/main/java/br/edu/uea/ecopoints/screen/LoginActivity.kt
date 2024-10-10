package br.edu.uea.ecopoints.screen

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityLoginBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var imageViewRoleIcon: ImageView
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var clCreateNewAccount: ConstraintLayout
    private lateinit var btLogin: MaterialButton

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
                else -> imageViewRoleIcon.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.worker_icon))
            }
        }
        setupListeners()
    }

    private fun setupListeners() {

    }

    private fun setupView() {
        imageViewRoleIcon = binding.ivRole
        edtEmail = binding.edtEmail
        edtPassword = binding.edtPassword
        clCreateNewAccount = binding.clCreateAccountText
    }
}