package br.edu.uea.ecopoints.screen.home.admin.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentEditProfileAdminBinding
import br.edu.uea.ecopoints.domain.entity.CoopAdmin
import br.edu.uea.ecopoints.screen.home.admin.HomeAdminViewModel
import br.edu.uea.ecopoints.screen.home.admin.state.EditProfileAdminState
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.EditProfileAdminViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileAdminFragment : Fragment() {
    private var _binding: FragmentEditProfileAdminBinding? = null
    private val binding get() = _binding!!

    private lateinit var edtName: TextInputEditText
    private var name: String = ""
    private lateinit var edtEmail: TextInputEditText
    private var email: String = ""
    private lateinit var edtPhone: TextInputEditText
    private var phone: String = ""
    private lateinit var edtPassword: TextInputEditText
    private var password: String = ""
    private lateinit var btnRegister: MaterialButton

    private val editProfileAdminViewModel: EditProfileAdminViewModel by viewModels()
    private val homeViewModel : HomeAdminViewModel by activityViewModels()

    @Inject lateinit var shared: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileAdminBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editProfileAdminViewModel.getAdminById()
        editProfileAdminViewModel.admin.observe(viewLifecycleOwner){ admin : CoopAdmin? ->
            if(admin!=null){
                edtName.setText(admin.name)
                edtEmail.setText(admin.email)
                edtPhone.setText(admin.phone)
                edtPassword.setText(shared.getString("password", "") ?: "")
            }
        }
        editProfileAdminViewModel.state.observe(viewLifecycleOwner){ state : EditProfileAdminState ->
            if(state.isProgressVisible){
                homeViewModel.state.value = HomeState.Loading
            } else{
                homeViewModel.state.value = HomeState.NotLoading
            }
            if(state.isErrorMessageVisible && state.errorDetails!=null){
                homeViewModel.state.value = HomeState.InconsistentInput(state.errorDetails!!,state.errorMessage!!)
            }
            if(state.adminUpdated!=null){
                homeViewModel.state.value = HomeState.Success(state.adminUpdated!!)
                AlertDialog.Builder(requireContext()).setTitle(
                    "SUCESSO"
                ).setMessage(
                    "Dados atualizados com sucesso"
                ).setPositiveButton("OK"){ dialog, _ -> dialog.dismiss() }.show()
                editProfileAdminViewModel.successAfter()
            }
        }
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            if(verifyInputs()){
                val name : String = edtName.text.toString()
                val email : String = edtEmail.text.toString()
                val phone: String? = if (edtPhone.text.isNullOrBlank()) null else edtPhone.text.toString()
                val password: String = edtPassword.text.toString()
                editProfileAdminViewModel.updateAdmin(name,email,phone,password)
            }
        }
    }

    private fun setupView() {
        edtName = binding.edtName
        edtEmail = binding.edtEmail
        edtPhone = binding.edtPhone
        edtPassword = binding.edtPassword
        btnRegister = binding.btEditProfile
    }

    private fun verifyInputs() : Boolean{
        var isValid = false
        if(!edtName.text.isNullOrBlank() &&
            !edtEmail.text.isNullOrBlank()){
            if(edtPassword.text!!.length < 7){
                edtPassword.error = "Senha muito curta (min 7)"
            } else {
                isValid = true
            }
        } else{
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}