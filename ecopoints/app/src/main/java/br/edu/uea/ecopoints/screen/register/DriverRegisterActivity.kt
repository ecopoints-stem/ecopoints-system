package br.edu.uea.ecopoints.screen.register

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityDriverRegisterBinding
import br.edu.uea.ecopoints.screen.state.register.DriverRegisterState
import br.edu.uea.ecopoints.screen.viewmodel.register.DriverRegisterViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DriverRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDriverRegisterBinding

    // Campos de entrada e seus respectivos Layouts e variáveis
    private lateinit var tilName: TextInputLayout
    private lateinit var edtName: TextInputEditText
    private var name: String = ""

    private lateinit var tilCnh: TextInputLayout
    private lateinit var edtCnh: TextInputEditText
    private var cnh: String = ""

    private lateinit var tilCpf: TextInputLayout
    private lateinit var edtCpf: TextInputEditText
    private var cpf: String = ""

    private lateinit var tilPhone: TextInputLayout
    private lateinit var edtPhone: TextInputEditText
    private var phone: String? = null

    private lateinit var tilEmail: TextInputLayout
    private lateinit var edtEmail: TextInputEditText
    private var email: String = ""

    private lateinit var tilPassword: TextInputLayout
    private lateinit var edtPassword: TextInputEditText
    private var password: String = ""

    // Botão de cadastrar
    private lateinit var btnSave: MaterialButton

    // ViewModel
    private val driverRegisterViewModel: DriverRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupListeners()
        driverRegisterViewModel.state.observe(this){
            state : DriverRegisterState ->
                binding.pbCls.isVisible = state.isProgressVisible
                state.driver?.let { driver ->
                    Log.i("ECO","Requisição bem sucedida")
                    Toast.makeText(this,"Usuário com email ${driver.email} cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                }
                if(state.isErrorMessageVisible){
                    Toast.makeText(this,"Erro ${state.errorMessage}", Toast.LENGTH_SHORT).show()
                    state.errorResponseApi?.let { error ->
                        val detailsMessage = error.details.entries.joinToString(separator = "\n") {
                            "${it.key}: ${it.value ?: "Informação não disponível"}"
                        }
                        AlertDialog.Builder(this).setTitle(
                            "ERRO STATUS ${error.status}"
                        ).setMessage(
                            detailsMessage
                        ).setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }.show()
                    }
                }
        }
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            if(verifyInputs()){
                populateParams()
                driverRegisterViewModel.save(
                    name = name,
                    phone = phone,
                    email = email,
                    password = password,
                    cpf = cpf,
                    cnh = cnh
                )
            } else{
                Toast.makeText(this,"Requisição não realizada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupView() {
        tilName = binding.tilName
        edtName = binding.edName

        tilCnh = binding.tilCnh
        edtCnh = binding.edCnh

        tilCpf = binding.tilCpf
        edtCpf = binding.edCpf

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
            !edtPassword.text.isNullOrBlank() &&
            !edtCpf.text.isNullOrBlank() &&
            !edtCnh.text.isNullOrBlank()){
            if(edtPassword.text!!.length < 7){
                edtPassword.error = "Senha muito curta (min 7)"
            } else {
                isValid = true
            }
        } else{
            if(edtCnh.text.isNullOrBlank()){
                edtCnh.error = "Campo obrigatório"
            }
            if(edtCpf.text.isNullOrBlank()){
                edtCpf.error = "Campo obrigatório"
            }
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

    private fun populateParams(){
        edtName.text?.toString()?.let {
            if(it.isNotBlank()){
                name = it
            }
        }
        edtCpf.text?.toString()?.let {
            if (it.isNotBlank()){
                cpf = it
            }
        }
        edtCnh.text?.toString()?.let {
            if (it.isNotBlank()){
                cnh = it
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