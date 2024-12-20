package br.edu.uea.ecopoints.screen.home.employee.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.edu.uea.ecopoints.databinding.FragmentEditProfileEmployeeBinding
import br.edu.uea.ecopoints.domain.entity.Employee
import br.edu.uea.ecopoints.screen.home.admin.HomeViewModel
import br.edu.uea.ecopoints.screen.home.employee.state.EditProfileEmployeeState
import br.edu.uea.ecopoints.screen.home.employee.viewmodel.EditProfileEmployeeViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileEmployeeFragment : Fragment() {
    private var _binding: FragmentEditProfileEmployeeBinding? = null
    private val binding get() = _binding!!

    private lateinit var edtName: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtCnpj: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnUpdate: MaterialButton

    private val editProfileEmployeeViewModel: EditProfileEmployeeViewModel by viewModels()
    private val homeViewModel : HomeViewModel by activityViewModels()

    @Inject lateinit var shared: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentEditProfileEmployeeBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editProfileEmployeeViewModel.getEmployeeId()
        editProfileEmployeeViewModel.state.observe(viewLifecycleOwner){ state: EditProfileEmployeeState ->
            state.employeeUpdated?.let { employee: Employee ->
                edtName.setText(employee.name)
                edtEmail.setText(employee.email)
                edtPhone.setText(employee.phone)
                lifecycleScope.launch {
                    edtCnpj.setText(editProfileEmployeeViewModel.getCnpjByCooperativeId(employee.cooperativeId))
                }
                edtPassword.setText(
                    shared.getString("password","")
                )
            }
            if(state.isProgressVisible){
                homeViewModel.state.value = HomeState.Loading
            } else{
                homeViewModel.state.value = HomeState.NotLoading
            }
            if(state.isErrorMessageVisible && state.errorDetails!=null){
                homeViewModel.state.value = HomeState.InconsistentInput(state.errorDetails!!,state.errorMessage!!)
            }
            if(state.employeeUpdated!=null){
                homeViewModel.state.value = HomeState.Success(state.employeeUpdated!!)
                AlertDialog.Builder(requireContext()).setTitle(
                    "SUCESSO"
                ).setMessage(
                    "Dados atualizados com sucesso"
                ).setPositiveButton("OK"){ dialog, _ -> dialog.dismiss() }.show()
                editProfileEmployeeViewModel.successAfter()
            }
        }
    }

    private fun setupView() {
        edtName = binding.edtName
        edtEmail = binding.edtEmail
        edtPhone = binding.edtPhone
        edtCnpj = binding.edtCnpj
        edtPassword = binding.edtPassword
        btnUpdate = binding.btEditProfile
    }

    private fun setupListeners() {
        btnUpdate.setOnClickListener {
            if(verifyInputs()){
                val name : String = edtName.text.toString()
                val email : String = edtEmail.text.toString()
                val phone: String? = if (edtPhone.text.isNullOrBlank()) null else edtPhone.text.toString()
                val cnpj : String? = if (edtCnpj.text.isNullOrBlank()) null else edtCnpj.text.toString()
                val password: String = edtPassword.text.toString()
                editProfileEmployeeViewModel.updateEmployee(name,email,phone,cnpj,password)
            }
        }
    }

    private fun verifyInputs() : Boolean {
        var isValid = false
        if(!edtName.text.isNullOrBlank() &&
            !edtEmail.text.isNullOrBlank()){
            if(edtPassword.text!!.length < 7){
                edtPassword.error = "Senha muito curta (min 7)"
            } else{
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
            if(edtCnpj.text.isNullOrBlank()){
                Toast.makeText(requireContext(), "Você não está associado a nenhuma cooperativa ? (Opcional)", Toast.LENGTH_SHORT).show()
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}