package br.edu.uea.ecopoints.screen.home.admin.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.edu.uea.ecopoints.R
import br.edu.uea.ecopoints.databinding.FragmentPickUpRequestBinding
import br.edu.uea.ecopoints.domain.entity.PickUpRequest
import br.edu.uea.ecopoints.domain.entity.enums.MaterialType
import br.edu.uea.ecopoints.screen.home.admin.HomeAdminViewModel
import br.edu.uea.ecopoints.screen.home.admin.fragment.recyclerview.PickUpAdapter
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.PickUpRequestViewModel
import br.edu.uea.ecopoints.screen.state.home.HomeState
import br.edu.uea.ecopoints.util.toMaterialType
import br.edu.uea.ecopoints.util.toPersonDate
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Locale

@AndroidEntryPoint
class PickUpRequestFragment : Fragment() {
    private var _binding: FragmentPickUpRequestBinding? = null
    private val binding get() = _binding!!
    private val adapter = PickUpAdapter()
    private val pickupViewModel: PickUpRequestViewModel by viewModels()
    private val homeViewModel: HomeAdminViewModel by activityViewModels()
    private lateinit var rc: RecyclerView
    private lateinit var btnAddPickUpRequest: FloatingActionButton

    //Propriedades da segunda tela
    private lateinit var edtCnpj: TextInputEditText
    private lateinit var tilCnpj: TextInputLayout
    private lateinit var edtEmailDriver: TextInputEditText
    private lateinit var tilEmailDriver: TextInputLayout
    private lateinit var edtPickUpAddress: TextInputEditText
    private lateinit var tilPickUpAddress: TextInputLayout
    private lateinit var spinnerMaterialType: Spinner
    private lateinit var edtQuantity: TextInputEditText
    private lateinit var tilQuantity: TextInputLayout
    private lateinit var edtUnitPrice: TextInputEditText
    private lateinit var tilUnitPrice: TextInputLayout
    private lateinit var btnDateCollect: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var btnCancel: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPickUpRequestBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        activateFirstScreen()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rc.adapter = adapter
        rc.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            pickupViewModel.pickups.collectLatest { paging: PagingData<PickUpRequest> ->
                adapter.submitData(paging)
            }
        }
        pickupViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isErrorMessageVisible) {
                homeViewModel.state.value =
                    HomeState.Failed(state.errorDetails, state.errorMessage ?: "Erro")
            } else {
                homeViewModel.state.value = HomeState.Success("Deu certo")
            }
            if (state.isProgressVisible) {
                homeViewModel.state.value = HomeState.Loading
            } else{
                homeViewModel.state.value = HomeState.Success("Deu certo")
            }
        }
    }

    private fun setupListeners() {
        btnAddPickUpRequest.setOnClickListener{
            activateSecondScreen()
        }
        btnCancel.setOnClickListener {
            activateFirstScreen()
        }
        btnSave.setOnClickListener {
            if(
                edtCnpj.text.isNullOrBlank() ||
                edtEmailDriver.text.isNullOrBlank() ||
                edtPickUpAddress.text.isNullOrBlank() ||
                edtQuantity.text.isNullOrBlank() ||
                edtUnitPrice.text.isNullOrBlank() ||
                !btnDateCollect.text.contains("/")
            ){
                Toast.makeText(requireContext(), "Os campos não foram preenchidos", Toast.LENGTH_SHORT).show()
            } else{
                // Posso enviar a requisição
                val cnpj : String = edtCnpj.text.toString()
                val emailDriver : String = edtEmailDriver.text.toString()
                val address: String = edtPickUpAddress.text.toString()
                val quantity: Double = edtQuantity.text.toString().toDouble()
                val unitPrice: BigDecimal = edtUnitPrice.text.toString().toBigDecimal()
                val dateCollect : LocalDate = btnDateCollect.text.toString().toPersonDate()!!
                val materialType : MaterialType = spinnerMaterialType.selectedItem.toString().toMaterialType()
                pickupViewModel.createNewPickUpRequest(cnpj,emailDriver,address,materialType,quantity,unitPrice,dateCollect)
                activateFirstScreen()
            }
        }
        btnDateCollect.setOnClickListener {
            showDatePicker {
                selectedDate -> btnDateCollect.text = selectedDate
            }
        }
    }

    private fun activateFirstScreen() {
        //Desativa segunda tela e ativa primeira tela
        tilCnpj.isVisible = false
        tilEmailDriver.isVisible = false
        tilPickUpAddress.isVisible = false
        tilQuantity.isVisible = false
        tilUnitPrice.isVisible = false
        btnDateCollect.isVisible = false
        btnSave.isVisible = false
        btnCancel.isVisible = false
        spinnerMaterialType.isVisible = false

        rc.isVisible = true
        btnAddPickUpRequest.isVisible = true
    }

    private fun activateSecondScreen() {
        //Desativa primeira tela e ativa segunda tela
        rc.isVisible = false
        btnAddPickUpRequest.isVisible = false

        tilCnpj.isVisible = true
        tilEmailDriver.isVisible = true
        tilPickUpAddress.isVisible = true
        tilQuantity.isVisible = true
        tilUnitPrice.isVisible = true
        btnDateCollect.isVisible = true
        spinnerMaterialType.isVisible = true
        btnSave.isVisible = true
        btnCancel.isVisible = true
    }

    private fun setupView() {
        rc = binding.rvPickupItems
        btnAddPickUpRequest = binding.fabAdd
        //Segunda tela
        edtCnpj = binding.edtCnpj
        tilCnpj = binding.tilCnpj
        edtEmailDriver = binding.edtEmailDriver
        tilEmailDriver = binding.tilEmailDriver
        edtPickUpAddress = binding.edtPickUpAddress
        tilPickUpAddress = binding.tilPickUpAddress
        spinnerMaterialType = binding.spMaterialType
        spinnerMaterialType.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.my_item_spinner,
                listOf("PAPEL", "METAL", "VIDRO", "ISOPOR", "PLÁSTICO")
            )
        )
        edtQuantity = binding.edtQuantity
        tilQuantity = binding.tilQuantity
        edtUnitPrice = binding.edtUnitPrice
        tilUnitPrice = binding.tilUnitPrice
        btnDateCollect = binding.btnDateCollect
        btnSave = binding.btnSave
        btnCancel = binding.btnCancel
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {
                _ , selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.ENGLISH, "%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}