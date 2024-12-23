package br.edu.uea.ecopoints.screen.home.driver.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentEditProfileDriverBinding
import br.edu.uea.ecopoints.domain.entity.Driver
import br.edu.uea.ecopoints.screen.home.admin.HomeViewModel
import br.edu.uea.ecopoints.screen.home.driver.state.EditProfileDriverState
import br.edu.uea.ecopoints.screen.home.driver.viewmodel.EditProfileDriverViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileDriverFragment : Fragment() {
    private var _binding: FragmentEditProfileDriverBinding? = null
    private val binding get() = _binding!!

    private lateinit var edtName: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPhone: TextInputEditText
    private lateinit var edtCnh: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var btnUpdate: MaterialButton

    private val editDriverViewModel: EditProfileDriverViewModel by viewModels()
    private val homeViewModel : HomeViewModel by activityViewModels()

    @Inject lateinit var shared: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, group: ViewGroup?, saved: Bundle?): View {
        _binding = FragmentEditProfileDriverBinding.inflate(inflater,group, false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editDriverViewModel.getDriverId()
        editDriverViewModel.state.observe(viewLifecycleOwner){ state: EditProfileDriverState ->
            state.driverUpdated?.let { driver: Driver ->
                edtName.setText(driver.name)
                edtEmail.setText(driver.email)
                edtPhone.setText(driver.phone)
                edtCnh.setText(driver.cnh)
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
            if(state.driverUpdated!=null && state.first){
                homeViewModel.state.value = HomeState.Success(state.driverUpdated!!)
                AlertDialog.Builder(requireContext()).setTitle(
                    "SUCESSO"
                ).setMessage(
                    "Dados atualizados com sucesso"
                ).setPositiveButton("OK"){ dialog, _ -> dialog.dismiss() }.show()
                editDriverViewModel.successAfter()
            }
        }
    }

    private fun setupView() {
        edtName = binding.edtName
        edtEmail = binding.edtEmail
        edtPhone = binding.edtPhone
        edtCnh = binding.edtCnh
        edtPassword = binding.edtPassword

        btnUpdate = binding.btEditProfile
    }

    private fun setupListeners() {
        btnUpdate.setOnClickListener {
            Log.i("ECO","clicou no botão")
            if(verifyInputs()){
                Log.i("ECO","Passou na verificação")
                val name : String = edtName.text.toString()
                val email : String = edtEmail.text.toString()
                val phone: String? = if (edtPhone.text.isNullOrBlank()) null else edtPhone.text.toString()
                val cnh : String = edtCnh.text.toString()
                val password : String = edtPassword.text.toString()
                editDriverViewModel.updateDriver(name, email, phone, cnh, password)
            }
        }
    }

    private fun verifyInputs() : Boolean {
        var isValid = false
        if(
            !edtName.text.isNullOrBlank() &&
            !edtEmail.text.isNullOrBlank() &&
            !edtCnh.text.isNullOrBlank()
        ){
            if(edtPassword.text!!.length < 7){
                edtPassword.error = "Senha muito curta (min 7)"
            } else{
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
            if(edtCnh.text.isNullOrBlank()){
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