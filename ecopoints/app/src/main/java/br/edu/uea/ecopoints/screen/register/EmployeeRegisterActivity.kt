package br.edu.uea.ecopoints.screen.register

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.ActivityEmployeeRegisterBinding
import br.edu.uea.ecopoints.screen.viewmodel.register.EmployeeRegisterViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmployeeRegisterActivity : AppCompatActivity() {
    private val binding : ActivityEmployeeRegisterBinding by lazy { ActivityEmployeeRegisterBinding.inflate(layoutInflater) }

    // Campos de entrada e seus respectivos Layouts e variáveis
    private lateinit var tilName: TextInputLayout
    private lateinit var edtName: TextInputEditText
    private var name: String = ""

    private lateinit var tilCnpj: TextInputLayout
    private lateinit var edtCnpj: TextInputEditText
    private var cnpj: String = ""

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
    private val employeeRegisterViewModel: EmployeeRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setupListeners()
    }

    private fun setupListeners() {

    }

    private fun setupView() {
        tilName = binding.tilName
        edtName = binding.edName

        tilCnpj = binding.tilCnpj
        edtCnpj = binding.edCnj

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

    private fun verifyInputs() : Boolean {
        var isValid = false

        return isValid
    }

    private fun eraseVariablesValues(){
        name = ""
        cnpj = ""
        cpf = ""
        phone = null
        email = ""
        password = ""
    }
}