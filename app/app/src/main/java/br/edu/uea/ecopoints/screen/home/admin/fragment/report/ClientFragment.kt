package br.edu.uea.ecopoints.screen.home.admin.fragment.report

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.edu.uea.ecopoints.databinding.FragmentAdminReportClientBinding
import br.edu.uea.ecopoints.screen.home.admin.viewmodel.report.ClientViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class ClientFragment :  Fragment() {
    private var _binding : FragmentAdminReportClientBinding? = null
    private val binding get() = _binding!!

    private lateinit var btnStartDate: MaterialButton
    private lateinit var btnEndDate: MaterialButton
    private lateinit var edtCnpj : TextInputEditText
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val clientViewModel: ClientViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminReportClientBinding.inflate(inflater,container,false)
        setupView()
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        btnStartDate.setOnClickListener {
            showDatePicker { selectedDate ->
                btnStartDate.text = selectedDate
            }
        }
        btnEndDate.setOnClickListener {
            showDatePicker { selectedDate ->
                try {
                    if(btnStartDate.text.contains("/")){
                        val start = dateFormat.parse(btnStartDate.text.toString())
                        val end = dateFormat.parse(selectedDate)
                        if (start != null && end != null && start.after(end)) {
                            Toast.makeText(requireContext(), "Data de início deve ser antes da data de fim", Toast.LENGTH_SHORT).show()
                        } else if (edtCnpj.text?.isNotBlank()==true){
                            Toast.makeText(requireContext(), "Campos de data validados, pode gerar", Toast.LENGTH_SHORT).show()
                        } else{
                            Toast.makeText(requireContext(), "Campos CNPJ em branco", Toast.LENGTH_SHORT).show()
                        }
                    } else{
                        Toast.makeText(requireContext(), "Campo de data de início não preenchido", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception){
                    Toast.makeText(requireContext(), "Erro ao validar datas", Toast.LENGTH_SHORT).show()
                }
                btnEndDate.text = selectedDate
            }
        }
    }

    private fun setupView() {
        btnStartDate = binding.btnStartDate
        btnEndDate = binding.btnEndDate
        edtCnpj = binding.edtCnpj
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.ENGLISH,"%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

}