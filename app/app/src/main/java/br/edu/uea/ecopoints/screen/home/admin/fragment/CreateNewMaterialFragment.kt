package br.edu.uea.ecopoints.screen.home.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.data.api.exception.ExceptionDetails
import br.edu.uea.ecopoints.databinding.FragmentCreateNewMaterialBinding
import br.edu.uea.ecopoints.screen.home.admin.HomeAdminViewModel
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.CreateNewMaterialViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import br.edu.uea.ecopoints.util.toMaterialType
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNewMaterialFragment : Fragment() {
    private var _binding: FragmentCreateNewMaterialBinding? = null
    private val binding get() = _binding!!
    private lateinit var spinnerNewMaterial: Spinner
    private lateinit var btnSave: MaterialButton
    private lateinit var edtMaterialName: TextInputEditText
    private val homeViewModel: HomeAdminViewModel by activityViewModels()
    private val newMaterialViewModel: CreateNewMaterialViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNewMaterialBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newMaterialViewModel.isLoadingVisible.observe(viewLifecycleOwner){ v ->
            if(v){
                homeViewModel.state.value = HomeState.Loading
            }
        }
        newMaterialViewModel.isErrorMessageVisible.observe(viewLifecycleOwner){ v ->
            if(v){
                val error = newMaterialViewModel.resultApi as? ExceptionDetails
                val message = newMaterialViewModel.errorMessage.value
                if(error!=null || message!=null){
                    homeViewModel.state.value = HomeState.Failed(error,message ?: "Erro em cadastro de novo material")
                }
            }
        }
        newMaterialViewModel.successMessage.observe(viewLifecycleOwner){ ms ->
            ms?.let {
                AlertDialog.Builder(requireContext()).setTitle(
                    "SUCESSO"
                ).setMessage(
                    it
                ).setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }.show()
                homeViewModel.state.value = HomeState.Success(ms)
                newMaterialViewModel.successAfter()
            }
        }
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            if(edtMaterialName.text?.isEmpty() == true){
                Toast.makeText(requireContext(),"Nome do novo material não pode ser nulo!!", Toast.LENGTH_SHORT).show()
            } else{
                val materialName = edtMaterialName.text.toString()
                newMaterialViewModel.saveMaterial(null, materialName, spinnerNewMaterial.selectedItem.toString().toMaterialType())
            }
        }
    }

    private fun setupView() {
        spinnerNewMaterial = binding.spNewMaterial
        spinnerNewMaterial.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.my_item_spinner,
                listOf("PAPEL", "METAL", "VIDRO", "ISOPOR", "PLÁSTICO")
            )
        )
        edtMaterialName = binding.edtNewMaterial
        btnSave = binding.btSaveMaterial
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}