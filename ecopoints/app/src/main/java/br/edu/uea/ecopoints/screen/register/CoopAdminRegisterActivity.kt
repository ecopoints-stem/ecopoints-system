package br.edu.uea.ecopoints.screen.register

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import br.edu.uea.ecopoints.databinding.ActivityCoopAdminRegisterBinding
import br.edu.uea.ecopoints.screen.viewmodel.register.AdminRegisterViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoopAdminRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoopAdminRegisterBinding

    //Campos de Entrada e seus respectivos Layouts
    private lateinit var tilName: TextInputLayout
    private lateinit var edtName: TextInputEditText

    private lateinit var tilCnpj: TextInputLayout
    private lateinit var edtCnpj: TextInputEditText

    private lateinit var tilCompanyName: TextInputLayout
    private lateinit var edtCompanyName: TextInputEditText

    private lateinit var tilSecurityQuestion: TextInputLayout
    private lateinit var edtSecurityQuestion: TextInputEditText

    private lateinit var tilSecurityResponse: TextInputLayout
    private lateinit var edtSecurityResponse: TextInputEditText

    private lateinit var tilPhone: TextInputLayout
    private lateinit var edtPhone: TextInputEditText

    private lateinit var tilEmail: TextInputLayout
    private lateinit var edtEmail: TextInputEditText

    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText

    // Botão de cadastrar
    private lateinit var btnSave: MaterialButton

    // ViewModel
    private val adminRegisterViewModel: AdminRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoopAdminRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListeners()
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            if(verifyInputs()){
                adminRegisterViewModel.save(
                    name = edtName.text!!.toString(),
                    phone = edtPhone.text?.toString(),
                    email = edtEmail.text!!.toString(),
                    password = edtPassword.text!!.toString(),
                    securityQuestion = edtSecurityQuestion.text?.toString(),
                    securityResponse = edtSecurityResponse.text?.toString(),
                    cooperativeName = edtCompanyName.text?.toString(),
                    cooperativeCnpj = null
                )
            }else{
                Toast.makeText(this,"Requisição não realizada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        tilName = binding.tilName
        edtName = binding.edName

        tilCnpj = binding.tilCnpj
        edtCnpj = binding.edCnpj

        tilCompanyName = binding.tilCompanyName
        edtCompanyName = binding.edCompanyName

        tilSecurityQuestion = binding.tilSecurityQuestion
        edtSecurityQuestion = binding.edSecurityQuestion

        tilSecurityResponse = binding.tilSecurityResponse
        edtSecurityResponse = binding.edSecurityResponse

        tilPhone = binding.tilPhone
        edtPhone = binding.edPhone

        tilEmail = binding.tilEmail
        edtEmail = binding.edEmail

        tilPassword = binding.tilPassword
        edtPassword = binding.edPassword

        btnSave = binding.btnRegister
    }

    private fun verifyInputs() : Boolean{
        var isValid = true

        if(edtName.text.isNullOrBlank()){
            isValid = false
            edtName.error = "Campo vazio"
        }
        if(edtCnpj.text.isNullOrBlank()){
            edtCnpj.text = null
        }
        if(edtCompanyName.text.isNullOrBlank()){
            edtCompanyName.text = null
        }
        if(edtSecurityQuestion.text.isNullOrBlank()){
            edtSecurityQuestion.text = null
        }
        if(edtSecurityResponse.text.isNullOrBlank()){
            edtSecurityResponse.text = null
        }
        if(edtPhone.text.isNullOrBlank()){
            edtPhone.text=null
        } else if(edtPhone.text.toString().length>=13){
            isValid = false
            edtPhone.error="Telefone não pode ter mais de 13 dígitos"
        }
        if(edtEmail.text.isNullOrBlank()){
            isValid = false
            tilEmail.error="Campo vazio"
        }
        if(edtPassword.text.isNullOrBlank()){
            isValid = false
            tilPassword.error = "Campo vazio"
        } else if(edtPassword.text.toString().length<7){
            tilPassword.error="Senha muito curta. Faça com pelo menos 7"
        }

        return isValid
    }
}