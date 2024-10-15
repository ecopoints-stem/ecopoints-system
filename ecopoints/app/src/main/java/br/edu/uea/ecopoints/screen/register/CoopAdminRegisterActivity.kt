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

    //Campos de Entrada e seus respectivos Layouts e variáveis
    private lateinit var tilName: TextInputLayout
    private lateinit var edtName: TextInputEditText
    private var name: String = ""

    private lateinit var tilCnpj: TextInputLayout
    private lateinit var edtCnpj: TextInputEditText
    private var cnpj: String? = null

    private lateinit var tilCompanyName: TextInputLayout
    private lateinit var edtCompanyName: TextInputEditText
    private var companyName: String? = null

    private lateinit var tilSecurityQuestion: TextInputLayout
    private lateinit var edtSecurityQuestion: TextInputEditText
    private var securityQuestion: String? = null

    private lateinit var tilSecurityResponse: TextInputLayout
    private lateinit var edtSecurityResponse: TextInputEditText
    private var securityResponse: String? = null

    private lateinit var tilPhone: TextInputLayout
    private lateinit var edtPhone: TextInputEditText
    private var phone: String? = null

    private lateinit var tilEmail: TextInputLayout
    private lateinit var edtEmail: TextInputEditText
    private var email : String = ""

    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText
    private var password: String = ""

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
                populateParams()
                adminRegisterViewModel.save(
                    name = name,
                    phone = phone,
                    email = email,
                    password = password,
                    securityQuestion = securityQuestion,
                    securityResponse = securityResponse,
                    cooperativeName = companyName,
                    cooperativeCnpj = cnpj
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
        var isValid = false
        if(!edtName.text.isNullOrBlank() &&
            !edtEmail.text.isNullOrBlank() &&
            !edtPassword.text.isNullOrBlank()){
            if(edtPassword.text!!.length < 7){
                edtPassword.error = "Senha muito curta (min 7)"
            } else {
                isValid = true
            }
        } else {
            if(edtName.text.isNullOrBlank()){
                edtName.error = "Campo obrigatório"
            }
            if(edtEmail.text.isNullOrBlank()){
                edtEmail.error = "Campo obrigatório"
            }
            if(edtPassword.text.isNullOrBlank()){
                edtPassword.error = "Campo obrigatório"
            } else if(edtPassword.text!!.length < 7){
                edtPassword.error = "Senha muito curta (min 7)"
            }
        }
        return isValid
    }

    private fun populateParams() {
        edtName.text?.toString()?.let {
            if(it.isNotBlank()){
                name = it
            }
        }
        edtCnpj.text?.toString()?.let {
            if (it.isNotBlank()){
                cnpj = it
            }
        }
        edtCompanyName.text?.toString()?.let {
            if(it.isNotBlank()){
                companyName = it
            }
        }
        edtSecurityQuestion.text?.toString()?.let {
            if(it.isNotBlank()){
                securityQuestion = it
            }
        }
        edtSecurityResponse.text?.toString()?.let {
            if(it.isNotBlank()){
                securityResponse = it
            }
        }
        edtPhone.text?.toString()?.let {
            if(it.isNotBlank()){
                phone = it
            }
        }
        edtEmail.text?.toString()?.let {
            if(it.isNotBlank()){
                email = it
            }
        }
        edtPassword.text?.toString()?.let {
            if(it.isNotBlank()){
                password = it
            }
        }
    }
}